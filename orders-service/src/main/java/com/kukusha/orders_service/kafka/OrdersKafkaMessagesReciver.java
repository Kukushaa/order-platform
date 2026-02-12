package com.kukusha.orders_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrdersKafkaMessagesReciver {
    @KafkaListener(topics = "orders.create", groupId = "orders-service")
    public void createNewOrder() {

    }
}
