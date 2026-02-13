package com.kukusha.product_service.response;

public record BuyProductResponse(String paymentToken, String clientSecret) {
}
