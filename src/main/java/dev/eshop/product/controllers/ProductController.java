package dev.eshop.product.controllers;

import dev.eshop.product.dtos.*;
import dev.eshop.product.dtos.product.GenericProductDto;
import dev.eshop.product.exceptions.NotFoundException;
import dev.eshop.product.security.JwtObject;
import dev.eshop.product.security.Role;
import dev.eshop.product.security.TokenValidator;
import dev.eshop.product.services.product.IProductService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {
    IProductService productService;
    private TokenValidator tokenValidator;


    public ProductController(@Qualifier("selfStoreProductService") IProductService productService, TokenValidator tokenValidator) {
        this.productService = productService;
        this.tokenValidator = tokenValidator;
    }

    @PostMapping(value = "/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody GenericProductDto productRequest) {
        log.info("addProduct :: Request For adding new Product Received with id {}", productRequest.getId());
        GenericProductDto productResponse = new GenericProductDto();
        try {
            productResponse = productService.createProduct(productRequest);
            return ResponseHandler.generateResponse("Product Added!", HttpStatus.CREATED, productResponse);
        } catch (Exception ex) {
            return ResponseHandler.generateResponse("Product Can't Be Added!", HttpStatus.BAD_REQUEST, productResponse);
        }
    }


    @GetMapping(value = "/getProductById/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") UUID uuid) throws NotFoundException {
        GenericProductDto productResponse = null;
        try {
            productResponse = productService.getProductById(uuid);
            return ResponseHandler.generateResponse("Successfully retrieved products!", HttpStatus.OK, productResponse);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, productResponse);
        }
    }

    @GetMapping(value = "/getAllProducts")
    public List<GenericProductDto> getAllProducts() {
        log.info("getAllProducts :: Get All Product Request Received");

        if (productService.getAllProducts().isEmpty()) {
            System.out.println("No products found in the database.");
            return new ArrayList<>();
        }
        return productService.getAllProducts();
    }


    @DeleteMapping(value = "/deleteById/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable UUID id) {
        try {
            productService.deleteProduct(id);
            return ResponseHandler.generateResponse("Deleted!", HttpStatus.OK, "");
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    @PutMapping(value = "/updateProductById/{id}")
    public ResponseEntity<?> updateProductById(@PathVariable("id") UUID id, @RequestBody GenericProductDto product) {

        try {
            productService.updateProduct(id, product);
            return ResponseHandler.generateResponse("Updated", HttpStatus.OK, "");
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @GetMapping(value = "/getProductsDetails")
    public List<GenericProductDto> getProductsDetails(@RequestHeader("AUTHORISATION") String authtoken,@RequestBody String userId) throws NotFoundException, URISyntaxException {
        System.out.println(authtoken);
        Optional<JwtObject> authTokenObjOptional;
        JwtObject authTokenObj = null;

        if (authtoken != null) {
            authTokenObjOptional = tokenValidator.validateToken(authtoken,userId);
            if (authTokenObjOptional.isEmpty()) {
               return productService.getAllProducts();
                //throw new RuntimeException("AUTH TOKEN IS NOT VALID !!");
            }
            authTokenObj = authTokenObjOptional.get();

        }
        //TODO: Implement tokenValidator

        if (productService.getAllProducts().isEmpty()) {
            System.out.println("No products found in the database.");
            return new ArrayList<>();
        }
        return productService.getAllProducts();
    }
}
