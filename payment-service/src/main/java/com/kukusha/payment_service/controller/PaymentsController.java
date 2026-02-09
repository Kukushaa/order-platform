package com.kukusha.payment_service.controller;

import com.kukusha.payment_service.dto.ConfirmPaymentRequest;
import com.kukusha.payment_service.dto.CreatePaymentResponse;
import com.kukusha.payment_service.dto.PaymentDTO;
import com.kukusha.payment_service.service.PaymentService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/payments")
public class PaymentsController {
    private final PaymentService paymentService;

    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<CreatePaymentResponse> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) throws StripeException {
        CreatePaymentResponse response = paymentService.createPayment(paymentDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = "/confirm")
    public ResponseEntity<Void> confirmPayment(@Valid @RequestBody ConfirmPaymentRequest request) throws StripeException {
        paymentService.confirmPayment(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
