package com.kukusha.payment_service.event;

import com.kukusha.payment_service.database.model.PaymentInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PaymentCompletedEvent extends ApplicationEvent {
    private final PaymentInfo paymentInfo;
    private final Long productId;

    public PaymentCompletedEvent(Object source, PaymentInfo paymentInfo, Long productId) {
        super(source);
        this.paymentInfo = paymentInfo;
        this.productId = productId;
    }
}
