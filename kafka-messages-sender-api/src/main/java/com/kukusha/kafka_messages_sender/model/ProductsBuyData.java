package com.kukusha.kafka_messages_sender.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductsBuyData extends KafkaMessageObject {
    private Long productId;
}
