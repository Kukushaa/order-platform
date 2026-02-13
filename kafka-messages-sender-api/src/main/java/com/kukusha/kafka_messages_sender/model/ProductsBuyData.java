package com.kukusha.kafka_messages_sender.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductsBuyData extends KafkaMessageObject {
    private Long productId;

    public ProductsBuyData(Long productId) {
        this.productId = productId;
    }

    public ProductsBuyData(String username, Long productId) {
        super(username);
        this.productId = productId;
    }
}
