package com.kukusha.kafka_messages_sender.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreatedData extends KafkaEmailMessageObject {
    private String productType;
    private long price;

    public ProductCreatedData(String username, String productType, long price) {
        super(username);
        this.productType = productType;
        this.price = price;
    }
}
