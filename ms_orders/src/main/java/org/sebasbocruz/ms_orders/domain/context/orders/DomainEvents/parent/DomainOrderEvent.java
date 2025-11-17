package org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.parent;

import java.time.Instant;

public abstract class DomainOrderEvent {

    private Long orderId;
    private double totalAmount;
    private String reason;
    Instant occurredOn;

    public DomainOrderEvent(Long orderId,Instant occurredOn) {
        this.orderId = orderId;
        this.occurredOn = occurredOn;
    }

    public DomainOrderEvent(Long orderId, String reason, Instant occurredOn) {
        this.orderId = orderId;
        this.reason = reason;
        this.occurredOn = occurredOn;
    }

    public DomainOrderEvent(Long orderId, double totalAmount, Instant occurredOn) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.occurredOn = occurredOn;
    }
}
