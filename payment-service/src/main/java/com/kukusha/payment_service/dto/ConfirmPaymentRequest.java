package com.kukusha.payment_service.dto;

import jakarta.validation.constraints.NotEmpty;

public record ConfirmPaymentRequest(@NotEmpty String paymentToken) {
}
