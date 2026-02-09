package com.kukusha.payment_service.event;

import com.kukusha.payment_service.database.model.PaymentInfo;
import org.springframework.context.ApplicationEvent;

public class PaymentCompletedEvent extends ApplicationEvent {
    private final PaymentInfo paymentInfo;

    public PaymentCompletedEvent(Object source, PaymentInfo paymentInfo) {
        super(source);
        this.paymentInfo = paymentInfo;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }
}
