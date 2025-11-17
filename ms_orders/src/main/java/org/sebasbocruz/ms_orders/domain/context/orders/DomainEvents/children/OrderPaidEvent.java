package org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.children;

import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.parent.DomainOrderEvent;

import java.time.Instant;

public class OrderPaidEvent extends DomainOrderEvent {
    public OrderPaidEvent(Long orderId, double totalAmount, Instant occurredOn) {
        super(orderId, totalAmount, occurredOn);
    }
}
