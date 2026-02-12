package com.kukusha.orders_service.controller;

import com.kukusha.orders_service.database.model.Order;
import com.kukusha.orders_service.database.model.OrderHistory;
import com.kukusha.orders_service.database.service.OrdersService;
import com.kukusha.orders_service.dto.UpdateOrderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.HashMap;
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
                                             @RequestBody UpdateOrderDTO dto) {
        Optional<Order> byId = ordersService.findById(id);

        if (byId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Order order = byId.get();
        OrderHistory history = new OrderHistory();

        history.setStatus(dto.getStatus());
        history.setManualChangeText(dto.getStatusChangeText());
        history.setDate(OffsetDateTime.now());

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

        OrderHistory.Status status = order.getOrderHistory().get(0).getStatus();

        if (status == OrderHistory.Status.DELIVERED) {
            Map<String, String> message = new HashMap<>();
            message.put("message", "Order has already been delivered");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }

        OrderHistory orderHistory = new OrderHistory();

        orderHistory.setStatus(status.next());
        orderHistory.setDate(OffsetDateTime.now());
        orderHistory.setOrder(order);

        order.addOrderHistory(orderHistory);

        ordersService.save(order);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
