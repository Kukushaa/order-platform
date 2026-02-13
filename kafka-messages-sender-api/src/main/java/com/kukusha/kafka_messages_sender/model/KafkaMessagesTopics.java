package com.kukusha.kafka_messages_sender.model;

import lombok.Getter;

@Getter
public enum KafkaMessagesTopics {
    REGISTER_USER("emails.register"),
    CREATE_PRODUCT("emails.product.create"),
    PAYMENT_COMPLETED("emails.payment.completed"),
    ORDER_CREATE("orders.create"),
    PRODUCT_BUY("product.buy");

    private final String key;

    KafkaMessagesTopics(String key) {
        this.key = key;
    }
}
