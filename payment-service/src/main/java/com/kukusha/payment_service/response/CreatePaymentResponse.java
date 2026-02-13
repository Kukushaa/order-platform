package com.kukusha.payment_service.response;

public record CreatePaymentResponse(String paymentToken, String clientSecret) {
}
