package com.kukusha.orders_service.controller;

import com.kukusha.orders_service.database.model.Order;
import com.kukusha.orders_service.database.service.OrdersService;
import com.kukusha.orders_service.dto.UpdateOrderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrdersController {
    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable String id, @RequestBody UpdateOrderDTO dto) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/{status}")
    public ResponseEntity<Void> updateStatusToOrder(@PathVariable String id,
                                                    @PathVariable String status) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
