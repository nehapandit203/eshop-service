package dev.eshop.product.services.product;

import dev.eshop.product.dtos.product.GenericProductDto;
import dev.eshop.product.exceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface IProductService {
    GenericProductDto createProduct(GenericProductDto product);

    GenericProductDto getProductById(UUID id) throws NotFoundException;

    List<GenericProductDto> getAllProducts();

    GenericProductDto deleteProduct(UUID id) throws NotFoundException;

    GenericProductDto updateProduct(UUID id, GenericProductDto product) throws NotFoundException;

    List<GenericProductDto> getAllProductsIfAuthorised();

}
