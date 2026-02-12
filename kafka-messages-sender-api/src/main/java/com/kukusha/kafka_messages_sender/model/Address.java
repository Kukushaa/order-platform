package com.kukusha.kafka_messages_sender.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address {
    private String address;
    private String city;
    private String state;
    private String country;
    private String zip;
}
