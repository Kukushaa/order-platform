package com.kukusha.payment_service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConfirmPaymentDTO {
    @NotBlank(message = "Payment token can't be empty")
    private String paymentToken;

    @NotNull(message = "Product ID can't be null!")
    private Long productId;
}
