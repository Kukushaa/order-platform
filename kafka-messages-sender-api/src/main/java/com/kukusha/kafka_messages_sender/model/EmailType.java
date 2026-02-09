package com.kukusha.kafka_messages_sender.model;

import lombok.Getter;

@Getter
public enum EmailType {
    REGISTER_USER("emails.register"),
    CREATE_PRODUCT("emails.product.create"),
    PAYMENT_COMPLETED("emails.payment.completed");

    private final String kakfaKey;

    EmailType(String kakfaKey) {
        this.kakfaKey = kakfaKey;
    }
}