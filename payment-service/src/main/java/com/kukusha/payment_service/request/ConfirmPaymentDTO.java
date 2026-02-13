package com.kukusha.payment_service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConfirmPaymentDTO {
    @NotBlank(message = "Payment token can't be empty")
    private String paymentToken;
}
