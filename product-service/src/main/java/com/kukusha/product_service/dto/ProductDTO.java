package com.kukusha.product_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDTO {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Product type is required")
    private String productType;

    @Min(value = 100, message = "Price must be minimum 1$")
    private long price;

    @Min(value = 1, message = "Amount wil lbe minimum 1")
    private long amount;
}
