package com.kukusha.orders_service.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "order_history")
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "manual_status_change_text")
    private String manualChangeText;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "date", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime date;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "oid", nullable = false)
    private Order order;

    public enum Status {
        PROCESSING,
        SHIPPED,
        IN_TRANSIT,
        OUT_FOR_DELIVERY,
        DELIVERED;

        public Status next() {
            Status[] values = Status.values();
            if (this.ordinal() >= values.length - 1) {
                return null;
            }

            return values[this.ordinal() + 1];
        }

        public boolean hasNext() {
            return this.ordinal() < values().length - 1;
        }
    }
}
