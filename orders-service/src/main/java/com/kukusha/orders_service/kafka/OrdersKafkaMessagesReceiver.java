package com.kukusha.orders_service.kafka;

import com.kukusha.kafka_messages_sender.model.NewOrderData;
import com.kukusha.orders_service.database.model.Order;
import com.kukusha.orders_service.database.service.OrdersService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrdersKafkaMessagesReceiver {
    private final OrdersService ordersService;

    public OrdersKafkaMessagesReceiver(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @KafkaListener(topics = "orders.create", groupId = "orders-service")
    public void createNewOrder(NewOrderData newOrderData) {
        Order order = new Order();

        order.setOwner(newOrderData.getUsername());
        order.setReceiver(newOrderData.getReceiver());
        order.setAddress(newOrderData.getAddress());

        ordersService.save(order);
    }
}
