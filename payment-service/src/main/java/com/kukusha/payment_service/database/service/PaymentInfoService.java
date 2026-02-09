package com.kukusha.payment_service.database.service;

import com.kukusha.payment_service.database.model.PaymentInfo;
import com.kukusha.payment_service.database.repository.PaymentInfoRepositroy;
import org.springframework.stereotype.Service;

@Service
public class PaymentInfoService {
    private final PaymentInfoRepositroy repositroy;

    public PaymentInfoService(PaymentInfoRepositroy repositroy) {
        this.repositroy = repositroy;
    }

    public void save(PaymentInfo paymentInfo) {
        repositroy.save(paymentInfo);
    }
}
