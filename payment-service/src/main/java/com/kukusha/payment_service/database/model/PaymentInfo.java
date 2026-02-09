package com.kukusha.payment_service.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String paymentId;
    private String clientSecret;
    @Column(name = "created_on", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdOn;

    public PaymentInfo() {
        this.createdOn = OffsetDateTime.now();
    }

    public enum Status {
        PROCESSING,
        ENDED,
        CANCELLED,
    }
}
