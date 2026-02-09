package com.kukusha.payment_service.service;

import com.kukusha.payment_service.dto.PaymentDTO;
import com.kukusha.token_service.service.TokenProcessorDriver;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    private final TokenProcessorDriver tokenProcessorDriver;

    public StripeService(TokenProcessorDriver tokenProcessorDriver) {
        this.tokenProcessorDriver = tokenProcessorDriver;
    }

    public PaymentIntent createPaymentIntent(PaymentDTO paymentDTO) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (paymentDTO.getAmount() / 100.0))
                .setCurrency(paymentDTO.getCurrency())
                .setReceiptEmail(paymentDTO.getEmail())
                .setDescription(createDescription(paymentDTO))
                .setAutomaticPaymentMethods(getAutomaticPaymentsMethods())
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        return intent;
    }

    private String createDescription(PaymentDTO paymentDTO) {
        return paymentDTO.getEmail() + " -- " + paymentDTO.getProductId() + " -- ";
    }

    private PaymentIntentCreateParams.AutomaticPaymentMethods getAutomaticPaymentsMethods() {
        return PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                .setEnabled(true)
                .build();
    }
}
