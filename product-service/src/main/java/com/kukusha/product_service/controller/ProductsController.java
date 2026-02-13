package com.kukusha.product_service.controller;

import com.kukusha.kafka_messages_sender.api.KafkaMessagesSenderAPI;
import com.kukusha.kafka_messages_sender.model.EmailType;
import com.kukusha.kafka_messages_sender.model.NewOrderData;
import com.kukusha.kafka_messages_sender.model.ProductCreatedData;
import com.kukusha.product_service.client.PaymentServiceClient;
import com.kukusha.product_service.database.model.Product;
import com.kukusha.product_service.database.service.ProductsService;
import com.kukusha.product_service.dto.BuyProductDTO;
import com.kukusha.product_service.dto.BuyProductResponse;
import com.kukusha.product_service.dto.CreatePaymentRequest;
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
    private final PaymentServiceClient paymentServiceClient;

    public ProductsController(ProductsService productsService,
                              KafkaMessagesSenderAPI kafkaMessagesSenderAPI,
                              UsersService usersService,
                              PaymentServiceClient paymentServiceClient) {
        this.productsService = productsService;
        this.kafkaMessagesSenderAPI = kafkaMessagesSenderAPI;
        this.usersService = usersService;
        this.paymentServiceClient = paymentServiceClient;
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
    public ResponseEntity<BuyProductResponse> buyProduct(@PathVariable Long id,
                                                         @RequestHeader(value = "Authorization") String authHeader,
                                                         @Valid @RequestBody BuyProductDTO buyProductDTO,
                                                         Principal principal) {
        String username = principal.getName();
        Optional<Product> productOptional = productsService.findById(id);

        if (productOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        Product product = productOptional.get();
        String productUsername = product.getUsername();
        if (productUsername.equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User can't buy own product!");
        }

        if (product.isNotInStock()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No product in stock!");
        }

        User buyer = usersService.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder()
                .amount(product.getPrice())
                .currency("usd")
                .email(buyer.getEmail())
                .productId(product.getId())
                .username(username)
                .build();

        BuyProductResponse response = paymentServiceClient.createPayment(paymentRequest, authHeader);
        kafkaMessagesSenderAPI.createOrder(new NewOrderData(productUsername, username, buyProductDTO.getAddress()));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
