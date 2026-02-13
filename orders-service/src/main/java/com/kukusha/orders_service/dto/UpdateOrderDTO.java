package com.kukusha.orders_service.dto;

import com.kukusha.orders_service.database.model.OrderHistory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateOrderDTO {
    @NotNull(message = "Status is required")
    private OrderHistory.Status status;

    @NotBlank(message = "Status change text is required")
    private String statusChangeText;
}
