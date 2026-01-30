package com.kukusha.procustservice.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kukusha.procustservice.dto.ProductDTO;
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
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(name = "product_type", nullable = false)
    private String productType;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    @JsonIgnore
    private String username;

    public Product(ProductDTO dto, String username) {
        BeanUtils.copyProperties(dto, this);
        this.username = username;
    }
}