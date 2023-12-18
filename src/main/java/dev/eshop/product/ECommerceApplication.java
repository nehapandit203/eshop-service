package dev.eshop.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@Slf4j
public class ECommerceApplication {

    public static void main(String[] args) {
       SpringApplication.run(ECommerceApplication.class, args);
        log.info("*************** Ecommerce Application Started ***********");

    }

}
