package com.kukusha.kafka_messages_sender.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreatedData extends KafkaMessageObject {
    private String productType;
    private String username;
    private long price;

    public ProductCreatedData(String username, String productType, String username1, long price) {
        super(username);
        this.productType = productType;
        this.username = username1;
        this.price = price;
    }
}
