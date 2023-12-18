package dev.eshop.product.dtos.product;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericProductDto {
    private UUID id ;
    private String title;
    private String description;
    private String image;
    private String category;
    private Double price;
    private String currency;
}