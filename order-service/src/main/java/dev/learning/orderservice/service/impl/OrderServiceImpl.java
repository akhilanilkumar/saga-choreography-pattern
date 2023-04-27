package dev.learning.orderservice.service.impl;

import dev.learning.model.event.OrderStatus;
import dev.learning.model.event.PaymentStatus;
import dev.learning.model.model.order.OrderRequest;
import dev.learning.orderservice.entity.PurchaseOrder;
import dev.learning.orderservice.repository.OrderRepository;
import dev.learning.orderservice.service.OrderService;
import dev.learning.orderservice.service.Publisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;

import static dev.learning.model.event.OrderStatus.ORDER_CANCELLED;
import static dev.learning.model.event.OrderStatus.ORDER_COMPLETED;
import static dev.learning.model.event.OrderStatus.ORDER_CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final Publisher orderPublisher;

    /**
     * Save & Publish the order to Cloud Stream
     *
     * @param orderRequest Order from client
     * @return Saved order
     */
    @Override
    @Transactional
    public PurchaseOrder createOrder(OrderRequest orderRequest) {
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .userId(orderRequest.getUserId())
                .price(orderRequest.getAmount())
                .productId(orderRequest.getProductId())
                .orderStatus(ORDER_CREATED)
                .build();
        PurchaseOrder savedOrder = orderRepository.save(purchaseOrder);
        // Mandatory for mapping when the response is received from payment-service
        orderRequest.setOrderId(savedOrder.getId());
        // Publish ORDER_EVENT topic to kafka
        log.info("ORDER-EVENT published: {}", orderRequest);
        orderPublisher.publishEvent(orderRequest, ORDER_CREATED);
        return savedOrder;
    }

    /**
     * Consume the Cloud stream event and update the order using order id
     *
     * @param id       Order id
     * @param consumer function to set payment status
     */
    @Override
    public void updateOrder(int id, Consumer<PurchaseOrder> consumer) {
        orderRepository.findById(id)
                .ifPresent(consumer.andThen(purchaseOrder -> {
                    boolean isPaymentCompleted = purchaseOrder.getPaymentStatus() == PaymentStatus.PAYMENT_COMPLETED;
                    OrderStatus orderStatus = isPaymentCompleted ? ORDER_COMPLETED : ORDER_CANCELLED;
                    purchaseOrder.setOrderStatus(orderStatus);
                    orderRepository.save(purchaseOrder);
                }));
    }

    @Override
    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAll();
    }
}
