package com.kukusha.order_service.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kukusha.order_service.dto.OrderDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", nullable = false)
    private String phoneNum;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(name = "order_type", nullable = false)
    private String orderType;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    @JsonIgnore
    private String username;

    public Order(OrderDTO dto, String username) {
        BeanUtils.copyProperties(dto, this);
        this.username = username;
    }
}