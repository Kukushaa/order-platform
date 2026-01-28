package com.kukusha.order_service.dto;

public record OrderDTO(String phoneNumber,
                       String email,
                       String description,
                       String orderType,
                       long price) {
}
