package org.sebasbocruz.ms_orders.domain.context.orders.Aggregate;

import lombok.Getter;
import lombok.Setter;
import org.sebasbocruz.ms_orders.domain.commons.errors.InvalidStateTransitionException;
import org.sebasbocruz.ms_orders.domain.commons.states.Order.OrderState;
import org.sebasbocruz.ms_orders.domain.commons.states.Order.ShipmentStatus;
import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.children.OrderCancelledEvent;
import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.children.OrderPaidEvent;
import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.parent.DomainOrderEvent;
import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.Currency;
import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.DeliveryAddress;
import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.DeliveryEstimate;
import org.sebasbocruz.ms_orders.domain.context.orders.entity.Shipment;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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


    public Order(
            Long orderId,
            Long clientId,
            Long userId,
            DeliveryAddress address,
            Currency currency,
            OrderState state,
            List<Shipment> shipments,
            Instant createdAt,
            Instant updatedAt
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
        this.updatedAt = updatedAt;
    }


    public static Order place(
            Long orderID,
            Long clientId,
            Long userId,
            DeliveryAddress deliveryAddress,
            List<Shipment> shipments,
            Currency currency,
            Clock clock
            ){

        if(shipments == null || shipments.isEmpty())
            throw new IllegalArgumentException("an order requires at least one shipment");

        Objects.requireNonNull(clock,"clock");

        Instant now = clock.instant();

        return new Order(
                orderID,
                clientId,
                userId,
                deliveryAddress,
                currency,
                OrderState.PENDING,
                shipments,
                now,
                now
        );


    }

    static Order rehydrate(
            Long id,
            Long clientId,
            Long createdBy,
            DeliveryAddress deliveryAddress,
            Currency currency,
            OrderState status,
            List<Shipment> shipments,
            Instant createdAt,
            Instant updatedAt
    ){
        return new Order(
                id, clientId, createdBy,
                deliveryAddress, currency,
                status,
                shipments,
                createdAt, updatedAt
        );
    }

    public void markAsPaid() {
        if (state != OrderState.PENDING) {
            throw new InvalidStateTransitionException("Order",state.name(),"PAID","ms_order");
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
    }



    private void registerEvent(DomainOrderEvent event) {
        this.domainEvents.add(event);
    }

    public DeliveryEstimate calculateEstimation(){
        List<Instant> ordered = this.shipments.stream()
                .map(Shipment::getEstimatedArrival)
                .sorted()
                .toList();

        return new DeliveryEstimate(
                ordered.getFirst().atOffset(ZoneOffset.UTC),
                ordered.getLast().atOffset(ZoneOffset.UTC)
        );

    }

    private void recomputeStatus(){
        if(this.state.equals(OrderState.CANCELLED)) return;
        Set<ShipmentStatus> states = shipments.stream().map(Shipment::getStatus).collect(Collectors.toUnmodifiableSet());
        if(states.equals(Set.of(ShipmentStatus.DELIVERED)))
            this.state  = OrderState.DELIVERED;
        else if(states.contains(ShipmentStatus.IN_TRANSIT) || states.contains(ShipmentStatus.DELIVERED))
            this.state = OrderState.SHIPPED;
        else
            this.state = OrderState.PROCESSING;

        touch();
    }

    public List<DomainOrderEvent> pullDomainEvents() {
        List<DomainOrderEvent> copy = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return copy;
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }
}
