package com.kukusha.procustservice.controller;

import com.kukusha.procustservice.database.model.Product;
import com.kukusha.procustservice.database.service.ProductsService;
import com.kukusha.procustservice.dto.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/products")
public class ProductsController {
    private static final String AUTH_HEADER_KEY = "Bearer ";
    private static final String EMPTY_STRING = "";
    private static final String USERNAME_CLAIMS_KEY = "username";

    private final ProductsService productsService;
    private final JwtDecoder jwtDecoder;

    public ProductsController(ProductsService productsService, JwtDecoder jwtDecoder) {
        this.productsService = productsService;
        this.jwtDecoder = jwtDecoder;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable,
                                                        @RequestHeader(value = "Authorization") String token) {
        String username = getUsernameFromToken(token);
        return new ResponseEntity<>(productsService.findAllByUsernameIsNot(username, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/me")
    public ResponseEntity<Page<Product>> getUsernameProducts(Pageable pageable,
                                                             @RequestHeader(value = "Authorization") String token) {
        String username = getUsernameFromToken(token);
        return new ResponseEntity<>(productsService.findAllByUsername(username, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> productOptional = productsService.findById(id);

        if (productOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid @RequestBody ProductDTO dto,
                                              @RequestHeader(value = "Authorization") String token) {
        String username = getUsernameFromToken(token);
        productsService.createNewProduct(dto, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getUsernameFromToken(String token) {
        return jwtDecoder.decode(token.replace(AUTH_HEADER_KEY, EMPTY_STRING)).getClaimAsString(USERNAME_CLAIMS_KEY);
    }
}
