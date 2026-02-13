package com.kukusha.kafka_messages_sender.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterSuccessfulEmailData extends KafkaEmailMessageObject {
    public RegisterSuccessfulEmailData(@JsonProperty("email") String email,
                                       @JsonProperty("username") String username) {
        super(username, email);
    }
}