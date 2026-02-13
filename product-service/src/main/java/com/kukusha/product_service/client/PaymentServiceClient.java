package com.kukusha.product_service.client;

import com.kukusha.product_service.response.BuyProductResponse;
import com.kukusha.product_service.request.CreatePaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${services.payment-service.name}", url = "${services.payment-service.url}")
public interface PaymentServiceClient {
    @PostMapping("/api/v1/payments")
    BuyProductResponse createPayment(@RequestBody CreatePaymentRequest request,
                                     @RequestHeader("Authorization") String authHeader);
}
