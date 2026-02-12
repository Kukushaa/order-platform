package com.kukusha.orders_service.dto;

import com.kukusha.orders_service.database.model.OrderHistory;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateOrderDTO {
    private OrderHistory.Status status;
    private String statusChangeText;
}
