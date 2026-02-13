package com.kukusha.product_service.dto;

import com.kukusha.kafka_messages_sender.model.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BuyProductDTO {
    @Valid
    @NotNull
    private Address address;
}
