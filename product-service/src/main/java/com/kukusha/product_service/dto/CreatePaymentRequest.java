package com.kukusha.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreatePaymentRequest {
    private Long amount;
    private String currency;
    private String email;
    private Long productId;
    private String username;
}
