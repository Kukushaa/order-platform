package com.kukusha.orders_service.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "oid")
    private Long id;

    private String owner;
    private String reciver;

    // Address dates
    private String address;
    private String city;
    private String state;
    private String country;
    private String zip;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderHistory> orderHistory;

    public Order() {
        this.orderHistory = new ArrayList<>();
    }

    public void addOrderHistory(OrderHistory orderHistory) {
        this.orderHistory.add(orderHistory);
    }
}