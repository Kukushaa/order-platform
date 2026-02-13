package com.kukusha.payment_service.database.repository;

import com.kukusha.payment_service.database.model.PaymentInfo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentInfoRepositroy extends JpaRepository<PaymentInfo, Long> {
    Optional<PaymentInfo> findByPaymentIntentId(String paymentIntentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PaymentInfo> findWithLockByPaymentIntentId(String paymentIntentId);
}
