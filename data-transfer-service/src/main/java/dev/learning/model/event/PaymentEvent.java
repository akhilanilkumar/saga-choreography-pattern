package dev.learning.model.event;

import dev.learning.model.model.payment.PaymentRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Data
public class PaymentEvent implements Event {

    private PaymentRequest paymentRequest;

    private PaymentStatus paymentStatus;

    private final UUID uuid = UUID.randomUUID();

    private final Date eventDate = new Date();

    public PaymentEvent(PaymentRequest paymentRequest, PaymentStatus paymentStatus) {
        this.paymentRequest = paymentRequest;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public UUID getEventId() {
        return null;
    }

    @Override
    public Date getEventDate() {
        return null;
    }
}
