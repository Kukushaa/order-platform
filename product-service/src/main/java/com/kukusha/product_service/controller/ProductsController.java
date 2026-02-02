package com.kukusha.product_service.controller;

import com.kukusha.product_service.database.model.Product;
import com.kukusha.product_service.database.service.ProductsService;
import com.kukusha.product_service.dto.ProductDTO;
import com.kukusha.token_service.model.TokenType;
import com.kukusha.token_service.service.TokenProcessorDriver;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final TokenProcessorDriver tokenProcessorDriver;

    public ProductsController(ProductsService productsService, TokenProcessorDriver tokenProcessorDriver) {
        this.productsService = productsService;
        this.tokenProcessorDriver = tokenProcessorDriver;
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
        String cleanToken = token.replace(AUTH_HEADER_KEY, EMPTY_STRING);
        return tokenProcessorDriver.getTokenProcessor(TokenType.ACCESS)
                .getClaimsAsString(cleanToken, USERNAME_CLAIMS_KEY);
    }
}
