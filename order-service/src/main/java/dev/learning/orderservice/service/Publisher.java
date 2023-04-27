package dev.learning.orderservice.service;

import dev.learning.model.event.OrderStatus;
import dev.learning.model.model.order.OrderRequest;

public interface Publisher {
    void publishEvent(OrderRequest request, OrderStatus status);
}
