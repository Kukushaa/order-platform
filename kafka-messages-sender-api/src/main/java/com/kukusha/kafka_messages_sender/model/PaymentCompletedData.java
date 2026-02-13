package com.kukusha.kafka_messages_sender.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentCompletedData extends KafkaEmailMessageObject {
    private String paymentIntentId;
    private Long productId;
    private long amount;

    public PaymentCompletedData(String username, String emailTo, String paymentIntentId, Long productId, long amount) {
        super(username, emailTo);
        this.paymentIntentId = paymentIntentId;
        this.productId = productId;
        this.amount = amount;
    }
}
