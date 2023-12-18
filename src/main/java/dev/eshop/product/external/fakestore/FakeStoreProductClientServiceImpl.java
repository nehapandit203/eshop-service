package dev.eshop.product.external.fakestore;

import dev.eshop.product.dtos.product.GenericProductDto;
import dev.eshop.product.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Service
@Qualifier("clientFakeStoreService")
public class FakeStoreProductClientServiceImpl implements IFakeStoreProductClientService {
    private RestTemplate restTemplate;
    @Value("${fakestore.product.url}")
    private String fakeStoreProductApi;

    public FakeStoreProductClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public FakeStoreProductDto createProduct(GenericProductDto product) {
        ResponseEntity<FakeStoreProductDto> response = restTemplate.postForEntity(
                fakeStoreProductApi, product, FakeStoreProductDto.class
        );
        return response.getBody();
    }

    @Override
    public FakeStoreProductDto getProductById(UUID id) throws NotFoundException {
        FakeStoreProductDto fakeStoreProductDto;
        fakeStoreProductDto =
                restTemplate.getForEntity(fakeStoreProductApi+"/{id}", FakeStoreProductDto.class, id).getBody();
        if (!Optional.ofNullable(fakeStoreProductDto).isPresent()) {
            throw new NotFoundException("Product with id: " + id + " doesn't exist!");
        }
        return fakeStoreProductDto;
    }

    @Override
    public List<FakeStoreProductDto> getAllProducts() {
        log.info("getAllProducts :: Get all products from external client service");
        ResponseEntity<FakeStoreProductDto[]> response =
                restTemplate.getForEntity(fakeStoreProductApi, FakeStoreProductDto[].class);
        return Arrays.asList(response.getBody());

    }

    @Override
    public void deleteProduct(Long id) {
        restTemplate.delete(fakeStoreProductApi+"/{id}", id);
    }

    public FakeStoreProductDto updateProduct(Long id, GenericProductDto product) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<GenericProductDto> entity = new HttpEntity<>(product, headers);
        return restTemplate.exchange(fakeStoreProductApi+"/{id}" + id, HttpMethod.PUT, entity, FakeStoreProductDto.class).getBody();
    }
}
