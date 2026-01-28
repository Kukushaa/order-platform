package com.kukusha.order_service.controller;

import com.kukusha.order_service.database.model.Order;
import com.kukusha.order_service.database.service.OrdersService;
import com.kukusha.order_service.dto.OrderDTO;
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
@RequestMapping(value = "/api/v1/orders")
public class OrdersController {
    private static final String AUTH_HEADER_KEY = "Bearer ";
    private static final String EMPTY_STRING = "";
    private static final String USERNAME_CLAIMS_KEY = "username";

    private final OrdersService ordersService;
    private final JwtDecoder jwtDecoder;

    public OrdersController(OrdersService ordersService, JwtDecoder jwtDecoder) {
        this.ordersService = ordersService;
        this.jwtDecoder = jwtDecoder;
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(Pageable pageable,
                                                    @RequestHeader(value = "Authorization") String token) {
        String username = getUsernameFromToken(token);
        return new ResponseEntity<>(ordersService.findAllByUsernameIsNot(username, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/me")
    public ResponseEntity<Page<Order>> getUsernameOrders(Pageable pageable,
                                                         @RequestHeader(value = "Authorization") String token) {
        String username = getUsernameFromToken(token);
        return new ResponseEntity<>(ordersService.findAllByUsername(username, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> orderOpt = ordersService.findById(id);

        if (orderOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        return new ResponseEntity<>(orderOpt.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@Valid @RequestBody OrderDTO dto,
                                            @RequestHeader(value = "Authorization") String token) {
        String username = getUsernameFromToken(token);
        ordersService.createNewOrder(dto, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getUsernameFromToken(String token) {
        return jwtDecoder.decode(token.replace(AUTH_HEADER_KEY, EMPTY_STRING)).getClaimAsString(USERNAME_CLAIMS_KEY);
    }
}
