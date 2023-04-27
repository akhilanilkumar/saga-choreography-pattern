package dev.learning.orderservice.config;

import dev.learning.model.event.PaymentEvent;
import dev.learning.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class EventConsumerConfig {

    private final OrderService orderService;

    /**
     * Listen to TOPIC payment-event, and check the payment status
     * if Payment-status COMPLETED ? Complete the order : Cancel the order
     *
     * @return Payment status
     */
    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer() {
        return paymentEvent -> orderService.updateOrder(
                paymentEvent.getPaymentRequest().getOrderId(),
                purchaseOrder -> purchaseOrder.setPaymentStatus(paymentEvent.getPaymentStatus())
        );
    }
}
