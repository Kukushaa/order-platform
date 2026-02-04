package com.kukusha.kafka_messages_sender.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class KafkaMessageObject {
    protected String username;

    public KafkaMessageObject(String username) {
        this.username = username;
    }
}
