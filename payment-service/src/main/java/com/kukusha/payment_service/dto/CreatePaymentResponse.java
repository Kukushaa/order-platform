package com.kukusha.payment_service.dto;

public record CreatePaymentResponse(String paymentToken, String clientSecret) {
}
