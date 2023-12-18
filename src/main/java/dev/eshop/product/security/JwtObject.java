package dev.eshop.product.security;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@Data
public class JwtObject {
    private String email;
    private Long userId;
    private Date createdAt;
    private Date expiryAt;
    private List<Role> roles = new ArrayList<>();
}