package dev.eshop.product.external.fakestore;

import dev.eshop.product.dtos.product.GenericProductDto;
import dev.eshop.product.exceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface IFakeStoreProductClientService {
    FakeStoreProductDto createProduct(GenericProductDto product);

    FakeStoreProductDto getProductById(UUID id) throws NotFoundException;

    List<FakeStoreProductDto> getAllProducts();

    void deleteProduct(Long id);

    FakeStoreProductDto updateProduct(Long id, GenericProductDto product);

}
