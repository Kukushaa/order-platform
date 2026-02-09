package com.kukusha.product_service.controller;

import com.kukusha.kafka_messages_sender.api.KafkaMessagesSenderAPI;
import com.kukusha.kafka_messages_sender.model.EmailType;
import com.kukusha.kafka_messages_sender.model.ProductCreatedData;
import com.kukusha.product_service.database.model.Product;
import com.kukusha.product_service.database.service.ProductsService;
import com.kukusha.product_service.dto.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/products")
public class ProductsController {
    private final ProductsService productsService;
    private final KafkaMessagesSenderAPI kafkaMessagesSenderAPI;

    public ProductsController(ProductsService productsService,
                              KafkaMessagesSenderAPI kafkaMessagesSenderAPI) {
        this.productsService = productsService;
        this.kafkaMessagesSenderAPI = kafkaMessagesSenderAPI;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable,
                                                        Principal principal) {
        return new ResponseEntity<>(productsService.findAllByUsernameIsNot(principal.getName(), pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/me")
    public ResponseEntity<Page<Product>> getUsernameProducts(Pageable pageable,
                                                             Principal principal) {
        return new ResponseEntity<>(productsService.findAllByUsername(principal.getName(), pageable), HttpStatus.OK);
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
                                              Principal principal) {
        String username = principal.getName();
        productsService.createNewProduct(dto, username);
        kafkaMessagesSenderAPI.sendEmail(EmailType.CREATE_PRODUCT, new ProductCreatedData(username, "emailTo", dto.productType(), dto.price()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
