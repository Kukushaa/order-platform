package com.kukusha.product_service.request;

import lombok.Builder;

@Builder
public record CreatePaymentRequest(Long amount,
                                   String currency,
                                   String email,
                                   Long productId,
                                   String username) {
}
