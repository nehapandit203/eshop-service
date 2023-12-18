package dev.eshop.product.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "price")
public class Price extends BaseModel{

    @Column(nullable = false,unique = false,length = 10,updatable = true)
    private String currency;

    @Column(nullable = false,length = 10,updatable = true)
    private Double price;


}