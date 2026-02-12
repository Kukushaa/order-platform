package com.kukusha.kafka_messages_sender.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewOrderData extends KafkaMessageObject {
    private String reciver;
    private Address address;

    public NewOrderData(String username, String reciver, Address address) {
        super(username);

        this.reciver = reciver;
        this.address = address;
    }
}
