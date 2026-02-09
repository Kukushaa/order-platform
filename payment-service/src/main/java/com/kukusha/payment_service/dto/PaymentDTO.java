package com.kukusha.payment_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentDTO {
    @NotNull
    private Long amount;

    @NotEmpty
    private String currency;

    @NotEmpty
    @Email
    private String email;

    private Long productId;

    private Map<String, String> metaData = new HashMap<>();

    public PaymentDTO() {
        this.currency = "usd";
    }
}
