package com.kukusha.kafka_messages_sender.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class KafkaEmailMessageObject extends KafkaMessageObject {
    protected String emailTo;

    public KafkaEmailMessageObject(String username, String emailTo) {
        super(username);
        this.emailTo = emailTo;
    }
}
