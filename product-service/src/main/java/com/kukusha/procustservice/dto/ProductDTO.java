package com.kukusha.procustservice.dto;

public record ProductDTO(String phoneNumber,
                         String email,
                         String description,
                         String productType,
                         long price) {
}
