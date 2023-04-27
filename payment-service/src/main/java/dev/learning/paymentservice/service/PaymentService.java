package dev.learning.paymentservice.service;

import dev.learning.model.event.OrderEvent;
import dev.learning.model.event.PaymentEvent;

public interface PaymentService {

    PaymentEvent createOrderEvent(OrderEvent orderEvent);

    void cancelOrderEvent(OrderEvent orderEvent);
}
