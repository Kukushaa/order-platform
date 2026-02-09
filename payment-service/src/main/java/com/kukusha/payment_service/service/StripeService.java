package com.kukusha.payment_service.service;

import com.kukusha.payment_service.dto.PaymentDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    public PaymentIntent createPaymentIntent(PaymentDTO paymentDTO) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(paymentDTO.getAmount())
                .setCurrency(paymentDTO.getCurrency())
                .setReceiptEmail(paymentDTO.getEmail())
                .setDescription(createDescription(paymentDTO))
                .setAutomaticPaymentMethods(getAutomaticPaymentsMethods())
                .build();

        return PaymentIntent.create(params);
    }

    public PaymentIntent retrievePaymentIntent(String paymentIntentId) throws StripeException {
        return PaymentIntent.retrieve(paymentIntentId);
    }

    private String createDescription(PaymentDTO paymentDTO) {
        return paymentDTO.getEmail() + " -- product:" + paymentDTO.getProductId();
    }

    private PaymentIntentCreateParams.AutomaticPaymentMethods getAutomaticPaymentsMethods() {
        return PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                .setEnabled(true)
                .build();
    }
}
