package dev.eshop.product.security;

import dev.eshop.product.dtos.SessionStatus;
import dev.eshop.product.dtos.ValidateTokenRequestDto;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SignatureException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j

public class TokenValidator {
    private RestTemplateBuilder restTemplateBuilder;

    @Value("${user.service.validate.url}")
    private String userUrl;

    public TokenValidator(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    /*   *
     * Calls user service to validate the token.
     * If token is not valid, optional is empty.
     * Else optional contains all of the data in payload
     * @param token
     * @return
     */
    public Optional<JwtObject> validateToken(String token, String userID) throws URISyntaxException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        URI url = new URI(userUrl);
        ValidateTokenRequestDto requestDto = ValidateTokenRequestDto.builder()
                .userId(Long.valueOf(userID))
                .token(token).build();

        HttpEntity<ValidateTokenRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

        SessionStatus statusResponseEntity = restTemplate.postForObject(url, requestEntity, SessionStatus.class);

        // if (statusResponseEntity.getStatusCode().is2xxSuccessful()) {
        if (statusResponseEntity.name().equalsIgnoreCase(SessionStatus.ACTIVE.name())) {
            Jws<Claims> claimsJws = validateToken(token);

            if (claimsJws != null) {
                String email = (String) claimsJws.getPayload().get("email");
                List<Role> roles = (List<Role>) claimsJws.getPayload().get("roles");
                Date createdAt = (Date) claimsJws.getPayload().get("createdAt");
                JwtObject jwt = JwtObject.builder().email(email).roles(roles).userId(Long.valueOf(userID)).build();

                return Optional.of(jwt);
            }
        }
        // }

        return Optional.empty();
    }

    public Jws<Claims> validateToken(String token) {
        Jws<Claims> claimsJws = null;
        try {
            claimsJws = Jwts.parser()
                    .build()
                    .parseSignedClaims(token);
            //Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return claimsJws;
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
            return claimsJws;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
            return claimsJws;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
            return claimsJws;
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
            return claimsJws;
        }

    }
}
