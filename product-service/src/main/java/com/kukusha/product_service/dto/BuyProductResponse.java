package com.kukusha.product_service.dto;

public record BuyProductResponse(String paymentToken, String clientSecret) {
}
