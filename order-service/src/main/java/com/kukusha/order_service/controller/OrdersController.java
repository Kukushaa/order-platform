package com.kukusha.order_service.controller;

import com.kukusha.order_service.database.model.Order;
import com.kukusha.order_service.database.service.OrdersService;
import com.kukusha.order_service.dto.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrdersController {
    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(Pageable pageable) {
        return new ResponseEntity<>(ordersService.findAll(pageable), HttpStatus.OK);
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
    public ResponseEntity<Void> createOrder(@Valid @RequestBody OrderDTO dto) {
        ordersService.createNewOrder(dto, "");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
