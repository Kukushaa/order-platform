package com.kukusha.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuyProductDTO {
    private Address address;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Address {
        private String street;
        private String city;
        private String state;
        private String country;
    }
}