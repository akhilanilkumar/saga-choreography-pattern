package dev.learning.model.event;

import dev.learning.model.model.order.OrderRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Data
public class OrderEvent implements Event {

    private OrderRequest orderRequest;

    private OrderStatus orderStatus;

    private final UUID uuid = UUID.randomUUID();

    private final Date eventDate = new Date();

    public OrderEvent(OrderRequest orderRequest, OrderStatus orderStatus) {
        this.orderRequest = orderRequest;
        this.orderStatus = orderStatus;
    }

    @Override
    public UUID getEventId() {
        return uuid;
    }

    @Override
    public Date getEventDate() {
        return eventDate;
    }
}
