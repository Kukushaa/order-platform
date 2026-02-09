package com.kukusha.payment_service.database.repository;

import com.kukusha.payment_service.database.model.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentInfoRepositroy extends JpaRepository<PaymentInfo, Long> {
}
