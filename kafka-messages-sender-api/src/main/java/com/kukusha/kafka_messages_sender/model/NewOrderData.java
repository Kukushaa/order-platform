package com.kukusha.kafka_messages_sender.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewOrderData extends KafkaMessageObject {
    private String receiver;
    private Address address;

    public NewOrderData(String username, String receiver, Address address) {
        super(username);

        this.receiver = receiver;
        this.address = address;
    }
}
