package com.kukusha.payment_service.event;

import com.stripe.model.PaymentIntent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PaymentCreatedEvent extends ApplicationEvent {
    private final PaymentIntent paymentIntent;
    private final String username;
    private final Long productId;
    private final long amount;
    private final String email;

    public PaymentCreatedEvent(Object source, PaymentIntent paymentIntent, String username, Long productId, long amount, String email) {
        super(source);
        this.paymentIntent = paymentIntent;
        this.username = username;
        this.productId = productId;
        this.amount = amount;
        this.email = email;
    }
}
