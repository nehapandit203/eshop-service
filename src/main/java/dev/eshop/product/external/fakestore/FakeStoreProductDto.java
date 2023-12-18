package dev.eshop.product.external.fakestore;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Data
@RequiredArgsConstructor
public class FakeStoreProductDto {
    private UUID id;
    private String title;
    private String description;
    private String image;
    private String category;
    private double price;
}