package org.sebasbocruz.ms_orders.domain.context.orders.Aggregate;

import lombok.Getter;
import lombok.Setter;
import org.sebasbocruz.ms_orders.domain.commons.states.Order.OrderState;
import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.children.OrderCancelledEvent;
import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.children.OrderCreatedEvent;
import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.children.OrderPaidEvent;
import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.parent.DomainOrderEvent;
import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.Currency;
import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.DeliveryAddress;
import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.DeliveryEstimate;
import org.sebasbocruz.ms_orders.domain.context.orders.entity.OrderDetail;
import org.sebasbocruz.ms_orders.domain.context.orders.entity.Shipment;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
public class Order {

    private Long orderId;
    private  Long clientId;
    private  Long userId;
    private final DeliveryAddress deliveryAddress;
    private final Currency currency;
    private OrderState state;
    private final List<Shipment> shipments;

    private double total;

    private  Instant createdAt;
    private Instant updatedAt;

    // ! domain events raised by this aggregate
    private final List<DomainOrderEvent> domainEvents = new ArrayList<>();

    // --------- ctor is private: use factory methods ----------
    public Order(
            Long orderId,
            Long clientId,
            Long userId,
            DeliveryAddress address,
            Currency currency,
            OrderState state,
            List<Shipment> shipments,
            Instant createdAt
    ) {

        if (clientId == null || userId == null ) {
            throw new IllegalArgumentException("Order requires client and user");
        }

        this.orderId = orderId;
        this.clientId = clientId;
        this.userId = userId;
        this.currency = currency;
        this.state = state;
        this.shipments = new ArrayList<>(shipments);
        this.deliveryAddress = address;
        calculateTotals();
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }


    public void markAsPaid() {
        if (state != OrderState.PENDING) {
            throw new IllegalStateException("Only PENDING orders can be paid");
        }
        this.state = OrderState.PAID;
        touch();
        registerEvent(new OrderPaidEvent(orderId, total, Instant.now()));
    }

    public void cancel(String reason) {
        if (state == OrderState.CANCELLED) return;
        this.state = OrderState.CANCELLED;
        touch();
        registerEvent(new OrderCancelledEvent(orderId, reason, Instant.now()));
    }

    private void calculateTotals() {
        double subtotal = shipments.stream()
                .map(Shipment::subtotal)
                .reduce(0.0, Double::sum);

        this.total = subtotal;
//        double taxableBase = subtotal - discount;
//        this.taxes = taxableBase*this.taxPercentage;
//        this.total = taxableBase+taxes;
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    private void registerEvent(DomainOrderEvent event) {
        this.domainEvents.add(event);
    }

    public DeliveryEstimate calculateEstimation(){
        List<Instant> orderded = this.shipments.stream()
                .map(shipment -> shipment.getEstimatedArrival())
                .sorted()
                .toList();

        return new DeliveryEstimate(
                orderded.getFirst().atOffset(ZoneOffset.UTC),
                orderded.getLast().atOffset(ZoneOffset.UTC)
        );

    }


    public List<DomainOrderEvent> pullDomainEvents() {
        List<DomainOrderEvent> copy = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return copy;
    }
}
