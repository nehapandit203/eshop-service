package dev.eshop.product.services.product;

import dev.eshop.product.external.fakestore.FakeStoreProductDto;
import dev.eshop.product.dtos.product.GenericProductDto;
import dev.eshop.product.exceptions.NotFoundException;
import dev.eshop.product.external.fakestore.IFakeStoreProductClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Qualifier("fakeStoreProductService")
public class FakeProductServiceImpl implements IProductService {
    private RestTemplate restTemplate;

    @Qualifier("clientFakeStoreService")
    private IFakeStoreProductClientService fakeStoreProductClient;

    public FakeProductServiceImpl(RestTemplate restTemplate,IFakeStoreProductClientService fakeStoreProductClient) {
        this.restTemplate = restTemplate;
        this.fakeStoreProductClient =fakeStoreProductClient;
    }

    @Override
    public GenericProductDto createProduct(GenericProductDto product) {
        return convertFakeStoreProductToGenericProduct(fakeStoreProductClient.createProduct(product));
    }

    @Override
    public GenericProductDto getProductById(UUID id) throws NotFoundException {
        FakeStoreProductDto fakeStoreProductDto;
        fakeStoreProductDto = fakeStoreProductClient.getProductById(id);
        if (!Optional.ofNullable(fakeStoreProductDto).isPresent()) {
            throw new NotFoundException("Product with id: " + id + " doesn't exist!");
        }
        return convertFakeStoreProductToGenericProduct(fakeStoreProductDto);
        //TODO: Handle Exceptions
    }

    @Override
    public List<GenericProductDto> getAllProducts() {
        log.info("getAllProducts :: get all products");

        return fakeStoreProductClient.getAllProducts().stream()
                .map(fakeStoreProductDto -> convertFakeStoreProductToGenericProduct(fakeStoreProductDto)).collect(Collectors.toList());
        //TODO: Handle Exceptions
    }

    @Override
    public GenericProductDto deleteProduct(UUID id) throws NotFoundException {
        return null;
    }

    @Override
    public GenericProductDto updateProduct(UUID id, GenericProductDto product) {
        return null;
    }

    @Override
    public List<GenericProductDto> getAllProductsIfAuthorised() {
        return null;
    }

    private GenericProductDto convertFakeStoreProductToGenericProduct(FakeStoreProductDto fakeStoreProductDto) {

        GenericProductDto product = GenericProductDto.builder()
                .id(fakeStoreProductDto.getId())
                .title(fakeStoreProductDto.getTitle())
                .image(fakeStoreProductDto.getImage())
                .category(fakeStoreProductDto.getCategory())
                .price(fakeStoreProductDto.getPrice())
                .description(fakeStoreProductDto.getDescription())
                .build();
        return product;
    }
}
