package com.kukusha.payment_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

    @NotNull
    private Long productId;

    @NotEmpty
    private String username;

    public PaymentDTO() {
        this.currency = "usd";
    }
}
