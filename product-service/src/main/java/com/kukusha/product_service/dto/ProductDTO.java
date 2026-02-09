package com.kukusha.product_service.dto;

public record ProductDTO(String phoneNumber,
                         String email,
                         String description,
                         String productType,
                         long price,
                         long amount) {
}
