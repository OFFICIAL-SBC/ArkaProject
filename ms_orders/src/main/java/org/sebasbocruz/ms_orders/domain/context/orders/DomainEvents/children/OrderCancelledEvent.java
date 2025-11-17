package org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.children;

import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.parent.DomainOrderEvent;

import java.time.Instant;

public class OrderCancelledEvent extends DomainOrderEvent {
    public OrderCancelledEvent(Long orderId, String reason, Instant occurredOn) {
        super(orderId, reason, occurredOn);
    }
}
