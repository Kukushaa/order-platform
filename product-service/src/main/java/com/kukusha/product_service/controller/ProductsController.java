package com.kukusha.product_service.controller;

import com.kukusha.kafka_messages_sender.api.KafkaMessagesSenderAPI;
import com.kukusha.kafka_messages_sender.model.EmailType;
import com.kukusha.kafka_messages_sender.model.ProductCreatedData;
import com.kukusha.product_service.database.model.Product;
import com.kukusha.product_service.database.service.ProductsService;
import com.kukusha.product_service.dto.ProductDTO;
import com.kukusha.users_shared_lib.model.User;
import com.kukusha.users_shared_lib.service.UsersService;
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
    private final UsersService usersService;

    public ProductsController(ProductsService productsService,
                              KafkaMessagesSenderAPI kafkaMessagesSenderAPI,
                              UsersService usersService) {
        this.productsService = productsService;
        this.kafkaMessagesSenderAPI = kafkaMessagesSenderAPI;
        this.usersService = usersService;
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
        User user = usersService.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")); // ADDITIONAL DEFENCE
        kafkaMessagesSenderAPI.sendEmail(EmailType.CREATE_PRODUCT, new ProductCreatedData(username, user.getEmail(), dto.productType(), dto.price()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/buy")
    public ResponseEntity<Void> buyProduct(@PathVariable Long id,
                                           Principal principal) {
        String username = principal.getName();
        Optional<Product> productOptional = productsService.findById(id);

        if (productOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        Product product = productOptional.get();
        if (product.getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can't buy own product!");
        }

        if (product.status().equals(Product.ProductStatus.NOT_IN_STOCK)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No product in stock!");
        }

        // TODO: If every check went well, call payment-service to create payment intent on Stripe
        // TODO: If payment went well: product amount - 1. Notify owner of selling product
        // TODO: If product amount == 0, product status: SELLED
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
