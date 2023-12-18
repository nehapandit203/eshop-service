package dev.eshop.product.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
public class ValidateTokenRequestDto {
    private Long userId;
    private String token;
}
