package dev.learning.paymentservice.config;

import dev.learning.model.event.OrderEvent;
import dev.learning.model.event.OrderStatus;
import dev.learning.model.event.PaymentEvent;
import dev.learning.paymentservice.entity.UserBalance;
import dev.learning.paymentservice.repository.UserBalanceRepository;
import dev.learning.paymentservice.service.PaymentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class PaymentConsumerConfig {

    private final PaymentService paymentService;

    private final UserBalanceRepository balanceRepository;

    /**
     * This function act as a consumer of topic: ORDER_EVENT
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

    @PostConstruct
    public void initUserBalance() {
        this.balanceRepository.saveAll(List.of(
                new UserBalance(100, 5000),
                new UserBalance(101, 5000),
                new UserBalance(102, 10000),
                new UserBalance(103, 1000),
                new UserBalance(104, 6000)));
    }
}
