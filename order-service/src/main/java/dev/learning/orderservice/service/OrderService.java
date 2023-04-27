package dev.learning.orderservice.service;

import dev.learning.model.model.order.OrderRequest;
import dev.learning.orderservice.entity.PurchaseOrder;

import java.util.List;
import java.util.function.Consumer;

public interface OrderService {

    PurchaseOrder createOrder(OrderRequest orderRequest);

    void updateOrder(int id, Consumer<PurchaseOrder> consumer);

    List<PurchaseOrder> getAllOrders();
}
