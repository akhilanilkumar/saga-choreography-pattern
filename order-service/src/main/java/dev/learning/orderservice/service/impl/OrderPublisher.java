package dev.learning.orderservice.service.impl;

import dev.learning.model.event.OrderEvent;
import dev.learning.model.event.OrderStatus;
import dev.learning.model.model.order.OrderRequest;
import dev.learning.orderservice.service.Publisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
public class OrderPublisher implements Publisher {

    private final Sinks.Many<OrderEvent> orderSink;


    @Override
    public void publishEvent(OrderRequest request, OrderStatus status) {
        OrderEvent orderEvent = new OrderEvent(request, status);
        orderSink.tryEmitNext(orderEvent);
    }
}
