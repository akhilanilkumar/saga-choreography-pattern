package dev.learning.paymentservice.config;

import dev.learning.model.event.OrderEvent;
import dev.learning.model.event.OrderStatus;
import dev.learning.model.event.PaymentEvent;
import dev.learning.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class PaymentConsumerConfig {

    private final PaymentService paymentService;

    /**
     * This function act as both consumer and producer of topic: ORDER_EVENT and PAYMENT_EVENT
     *
     * @return PaymentEvent
     */
    @Bean
    public Function<Flux<OrderEvent>, Flux<PaymentEvent>> paymentProcessor() {
        return orderEventFlux -> orderEventFlux.flatMap(orderEvent -> {
            if (orderEvent.getOrderStatus() == OrderStatus.ORDER_CREATED) {
                return Mono.fromSupplier(() -> this.paymentService.createOrderEvent(orderEvent));
            } else {
                return Mono.fromRunnable(() -> this.paymentService.cancelOrderEvent(orderEvent));
            }
        });
    }
}
