package com.kukusha.kafka_messages_sender.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RegisterSuccessfullEmailData extends KafkaMessageObject {
    private final String email;
    private final String username;

    @JsonCreator
    public RegisterSuccessfullEmailData(@JsonProperty("email") String email,
                                        @JsonProperty("username") String username) {
        this.email = email;
        this.username = username;
    }
}