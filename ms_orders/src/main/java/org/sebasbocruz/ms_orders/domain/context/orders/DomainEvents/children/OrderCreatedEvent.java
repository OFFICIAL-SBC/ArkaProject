package org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.children;

import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.parent.DomainOrderEvent;

import java.time.Instant;

public class OrderCreatedEvent extends DomainOrderEvent {
    public OrderCreatedEvent(Long orderId, Instant occurredOn) {
        super(orderId,occurredOn);
    }
}
