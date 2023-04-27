package dev.learning.model.model.order;

import dev.learning.model.event.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private int userId;

    private int productId;

    private int amount;

    private int orderId;

    private OrderStatus orderStatus;
}
