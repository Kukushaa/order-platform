package com.kukusha.orders_service.controller;

import com.kukusha.orders_service.database.model.Order;
import com.kukusha.orders_service.database.model.OrderHistory;
import com.kukusha.orders_service.database.service.OrdersService;
import com.kukusha.orders_service.dto.UpdateOrderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrdersController {
    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> byId = ordersService.findById(id);
        return byId.map(order -> new ResponseEntity<>(order, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id,
                                             @Valid @RequestBody UpdateOrderDTO dto) {
        Optional<Order> byId = ordersService.findById(id);

        if (byId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Order order = byId.get();
        OrderHistory history = new OrderHistory();

        history.setStatus(dto.getStatus());
        history.setManualChangeText(dto.getStatusChangeText());
        history.setDate(OffsetDateTime.now());
        history.setOrder(order);

        order.addOrderHistory(history);

        ordersService.save(order);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/nextStatus")
    public ResponseEntity<?> updateStatusToOrder(@PathVariable Long id) {
        Optional<Order> byId = ordersService.findById(id);

        if (byId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Order order = byId.get();

        List<OrderHistory> orderHistory = order.getOrderHistory();

        if (orderHistory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        OrderHistory.Status status = orderHistory.getFirst().getStatus();

        if (status == OrderHistory.Status.DELIVERED) {
            Map<String, String> message = new HashMap<>();
            message.put("message", "Order has already been delivered");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }

        OrderHistory newOrderHistory = new OrderHistory();

        newOrderHistory.setStatus(status.next());
        newOrderHistory.setDate(OffsetDateTime.now());
        newOrderHistory.setOrder(order);

        order.addOrderHistory(newOrderHistory);

        ordersService.save(order);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
