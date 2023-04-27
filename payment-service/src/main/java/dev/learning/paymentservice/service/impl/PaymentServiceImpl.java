package dev.learning.paymentservice.service.impl;

import dev.learning.model.event.OrderEvent;
import dev.learning.model.event.PaymentEvent;
import dev.learning.model.event.PaymentStatus;
import dev.learning.model.model.order.OrderRequest;
import dev.learning.model.model.payment.PaymentRequest;
import dev.learning.paymentservice.entity.UserTransaction;
import dev.learning.paymentservice.repository.UserBalanceRepository;
import dev.learning.paymentservice.repository.UserTransactionRepository;
import dev.learning.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserBalanceRepository balanceRepository;

    private final UserTransactionRepository transactionRepository;

    /**
     * Get USER_ID
     * CHECK BALANCE
     * Payment Completed --> When sufficient balance, else CANCEL payment
     * Update the DB
     *
     * @param orderEvent Order Event
     * @return Payment Event
     */
    @Override
    @Transactional
    public PaymentEvent createOrderEvent(OrderEvent orderEvent) {
        OrderRequest orderRequest = orderEvent.getOrderRequest();
        PaymentRequest paymentRequest = new PaymentRequest(orderRequest.getOrderId(), orderRequest.getUserId(), orderRequest.getAmount());
        return balanceRepository.findById(orderRequest.getUserId())
                .filter(userBalance -> userBalance.getPrice() >= orderRequest.getAmount())
                .map(userBalance -> {
                    userBalance.setPrice(userBalance.getPrice() - orderRequest.getAmount());
                    transactionRepository.save(new UserTransaction(orderRequest.getOrderId(), orderRequest.getUserId(), orderRequest.getAmount()));
                    return new PaymentEvent(paymentRequest, PaymentStatus.PAYMENT_COMPLETED);
                })
                .orElse(new PaymentEvent(paymentRequest, PaymentStatus.PAYMENT_FAILED));
    }

    @Override
    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent) {
        transactionRepository.findById(orderEvent.getOrderRequest().getOrderId())
                .ifPresent(userTransaction -> {
                    transactionRepository.delete(userTransaction);
                    transactionRepository.findById(userTransaction.getUserId())
                            .ifPresent(ut -> ut.setAmount(ut.getAmount() + userTransaction.getAmount()));
                });
    }
}
