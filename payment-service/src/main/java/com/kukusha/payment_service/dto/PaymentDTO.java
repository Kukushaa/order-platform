package com.kukusha.payment_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @Min(value = 100, message = "Price must be minimum 1$")
    private long amount;

    @NotBlank(message = "Currency can't be empty")
    private String currency;

    @NotBlank(message = "Email can't be empty")
    @Email(message = "Invalid email")
    private String email;

    @NotNull(message = "Product ID can't be null")
    private Long productId;

    @NotBlank(message = "Username can't be empty")
    private String username;

    public PaymentDTO() {
        this.currency = "usd";
    }
}
