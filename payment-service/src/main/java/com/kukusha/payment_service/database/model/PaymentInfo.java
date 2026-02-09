package com.kukusha.payment_service.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Entity
@Table(name = "payment_info")
public class PaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_intent_id", nullable = false, unique = true)
    private String paymentIntentId;

    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    @Column(nullable = false)
    private String username;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdOn;

    public PaymentInfo() {
        this.createdOn = OffsetDateTime.now();
        this.status = Status.PROCESSING;
    }

    public enum Status {
        PROCESSING,
        COMPLETED,
        CANCELLED
    }
}
