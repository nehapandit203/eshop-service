package dev.eshop.product.services.product;

import dev.eshop.product.dtos.product.GenericProductDto;
import dev.eshop.product.exceptions.NotFoundException;
import dev.eshop.product.models.Category;
import dev.eshop.product.models.Price;
import dev.eshop.product.models.Product;
import dev.eshop.product.repositories.CategoryRepository;
import dev.eshop.product.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Qualifier("selfStoreProductService")
public class SelfProductServiceImpl implements IProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public SelfProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public GenericProductDto createProduct(GenericProductDto genericProductDto) {
       Optional<Category> optionalCategory = categoryRepository.findByNameIgnoreCase(genericProductDto.getCategory());

        Category category;
        if (optionalCategory.isPresent()) {
            category = optionalCategory.get();
        } else {
            category = new Category();
            category.setName(genericProductDto.getCategory());
            categoryRepository.save(category);
        }
        Product product = new Product();
        //product.se(UUID.randomUUID());
        product.setTitle(genericProductDto.getTitle());
        product.setDescription(genericProductDto.getDescription());
        product.setImage(genericProductDto.getImage());

        Price price = new Price();
        price.setPrice(genericProductDto.getPrice());
        price.setCurrency(genericProductDto.getCurrency());
        product.setPrice(price);

        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return convertProductToGenericProduct(savedProduct);

    }

    @Override
    public GenericProductDto getProductById(UUID id) throws NotFoundException {
        Product product = productRepository.findById(UUID.fromString(id.toString()))
                .orElseThrow(() -> new NotFoundException("Product with ID: " + id + " not found. Please try again."));

        return convertProductToGenericProduct(product);
    }

    @Override
    public List<GenericProductDto> getAllProducts() {//Long userIdTryingToAccess
        log.info("getAllProducts :: get all products");

        List<Product> products = productRepository.findAll();

        if(products.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        return products.stream()
                .map(selfProductDto -> convertProductToGenericProduct(selfProductDto)).collect(Collectors.toList());

    }

    @Override
    public GenericProductDto deleteProduct(UUID id) throws NotFoundException {

        Optional<Product> optionalProduct = productRepository.findById(UUID.fromString(String.valueOf(id)));
        if (optionalProduct.isEmpty())
            throw new NotFoundException("Product with ID : " + id + " NOT FOUND, Deletion Failed.");

        Product product = optionalProduct.get();

        GenericProductDto productDto = new GenericProductDto();
        productDto.setTitle(product.getTitle());
        productDto.setDescription(product.getDescription());
        productDto.setImage(product.getImage());
        productDto.setPrice(product.getPrice().getPrice());
        productDto.setCategory(product.getCategory().getName());

        productRepository.delete(product);
        return productDto;

    }

    @Override
    public GenericProductDto updateProduct(UUID id, GenericProductDto genericProductDto) throws NotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(UUID.fromString(String.valueOf(id)));
        if (optionalProduct.isEmpty())
            throw new NotFoundException("Product with ID : " + id + " NOT FOUND, Update Failed.");

        Product product = optionalProduct.get();

        // checking if uuid != product.getUuid() then throw exception
        if (!id.equals(product.getUuid()))
            throw new NotFoundException("Product ID : " + id + " does not match the Request Body ID : " + product.getUuid() + " in the database. Update Failed.");

        // get the category provided by user
//        String categoryValue = genericProductDto.getCategory();
        Optional<Category> optionalCategory = categoryRepository.findByNameIgnoreCase(genericProductDto.getCategory());

        Category category;
        if (optionalCategory.isPresent()) {
            category = optionalCategory.get();
        } else {
            category = new Category();
            category.setName(genericProductDto.getCategory());
            categoryRepository.save(category);
        }

        product.setUuid(id);
        product.setTitle(genericProductDto.getTitle());
        product.setDescription(genericProductDto.getDescription());
        product.setImage(genericProductDto.getImage());
        Price price = new Price();
        price.setPrice(genericProductDto.getPrice());
        price.setCurrency(genericProductDto.getCurrency());
        product.setPrice(price);

        category.setName(genericProductDto.getCategory());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return copyProductDtoProperties(savedProduct);
    }

    @Override
    public List<GenericProductDto> getAllProductsIfAuthorised() {

        List<Product> products= productRepository.findAll();
        if(products.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        return products.stream()
                .map(selfProductDto -> convertProductToGenericProduct(selfProductDto)).collect(Collectors.toList());

    }

    private GenericProductDto copyProductDtoProperties(Product source) {
        GenericProductDto target = new GenericProductDto();
        target.setId(source.getUuid());
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setImage(source.getImage());
        target.setPrice(source.getPrice().getPrice());
        target.setCurrency(source.getPrice().getCurrency());
        target.setCategory(source.getCategory().getName());
        return target;
    }


    private GenericProductDto convertProductToGenericProduct(Product product) {

        GenericProductDto genericProductDto = new GenericProductDto();
        genericProductDto.setId(product.getUuid());
        genericProductDto.setTitle(product.getTitle());
        genericProductDto.setDescription(product.getDescription());
        genericProductDto.setImage(product.getImage());
        genericProductDto.setPrice(product.getPrice().getPrice());
        genericProductDto.setCurrency(product.getPrice().getCurrency());
        //genericProductDto.setCategory(product.getCategory_id().getName());
        return genericProductDto;
    }

}
