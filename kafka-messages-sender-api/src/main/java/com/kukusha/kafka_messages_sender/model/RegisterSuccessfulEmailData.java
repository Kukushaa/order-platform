package com.kukusha.kafka_messages_sender.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RegisterSuccessfulEmailData extends KafkaEmailMessageObject {
    public RegisterSuccessfulEmailData(@JsonProperty("email") String email,
                                       @JsonProperty("username") String username) {
        super(username, email);
    }
}