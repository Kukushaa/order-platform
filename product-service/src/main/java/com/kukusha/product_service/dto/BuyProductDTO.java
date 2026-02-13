package com.kukusha.product_service.dto;

import com.kukusha.kafka_messages_sender.model.Address;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BuyProductDTO {
    private Address address;
}
