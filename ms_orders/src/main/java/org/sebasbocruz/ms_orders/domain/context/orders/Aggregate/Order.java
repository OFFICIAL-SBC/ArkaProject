package org.sebasbocruz.ms_orders.domain.context.orders.Aggregate;

import lombok.Getter;
import lombok.Setter;
import org.sebasbocruz.ms_orders.domain.commons.OrderState;
import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.children.OrderCancelledEvent;
import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.children.OrderCreatedEvent;
import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.children.OrderPaidEvent;
import org.sebasbocruz.ms_orders.domain.context.orders.DomainEvents.parent.DomainOrderEvent;
import org.sebasbocruz.ms_orders.domain.context.orders.entity.OrderDetail;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Order {

    private final Long orderId;
    private final Long clientId;
    private final Long userId;
    private final Long warehouseId;
    private final Long currencyId;

    private OrderState state;
    private double total;
    private double taxes;
    private double taxPercentage;
    private double discount;

    private final Instant createdAt;
    private Instant updatedAt;

    private final List<OrderDetail> details = new ArrayList<>();

    // domain events raised by this aggregate
    private final List<DomainOrderEvent> domainEvents = new ArrayList<>();

    // --------- ctor is private: use factory methods ----------
    private Order(
            Long orderId,
            Long clientId,
            Long userId,
            Long warehouseId,
            Long currencyId,
            OrderState state,
            double total,
            double taxes,
            double taxPercentage,
            double discount,
            Instant createdAt,
            Instant updatedAt
    ) {

        if (clientId == null || userId == null || warehouseId == null || currencyId == null) {
            throw new IllegalArgumentException("Order requires client, user, warehouse and currency");
        }

        this.orderId = orderId;
        this.clientId = clientId;
        this.userId = userId;
        this.warehouseId = warehouseId;
        this.currencyId = currencyId;
        this.state = state;
        this.total = total;
        this.taxes = taxes;
        this.taxPercentage = taxPercentage;
        this.discount = discount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --------- FACTORY METHODS ----------

    public static Order createNew(
            Long clientId,
            Long userId,
            Long warehouseId,
            Long currencyId
    ) {
        Instant now = Instant.now();
        Order order = new Order(
                null,
                clientId,
                userId,
                warehouseId,
                currencyId,
                OrderState.PENDING,
                0,
                0,
                0,
                0,
                now,
                now
        );

        order.registerEvent(new OrderCreatedEvent(order.orderId, now));
        return order;
    }

    // rehydrate from persistence
    public static Order restore(
            Long orderId,
            Long clientId,
            Long userId,
            Long warehouseId,
            Long currencyId,
            OrderState state,
            double total,
            double taxes,
            double taxPercentage,
            double discount,
            Instant createdAt,
            Instant updatedAt,
            List<OrderDetail> details
    ) {
        Order order = new Order(orderId, clientId, userId, warehouseId, currencyId,
                state, total, taxes,  discount,taxPercentage, createdAt, updatedAt);
        order.details.addAll(details);
        return order;
    }

    // --------- BEHAVIOR ----------

    public void addItem(Long productId, int amount, double unitPrice) {
        if (state != OrderState.PENDING) {
            throw new IllegalStateException("Can only modify items when order is PENDING");
        }
        OrderDetail detail = new OrderDetail(null, productId, amount, unitPrice);
        this.details.add(detail);
        recalculateTotals();
        touch();
    }

    public void removeItem(Long productId) {
        if (state != OrderState.PENDING) {
            throw new IllegalStateException("Can only modify items when order is PENDING");
        }
        details.removeIf(d -> d.getProductId().equals(productId));
        recalculateTotals();
        touch();
    }

    public void applyDiscount(double discount) {
        if (discount< 0) throw new IllegalArgumentException("Discount cannot be negative");
        this.discount = discount;
        recalculateTotals();
        touch();
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

    private void recalculateTotals() {
        double subtotal = details.stream()
                .map(OrderDetail::getLineTotal)
                .reduce(0.0, Double::sum);

        double taxableBase = subtotal - discount;
        this.taxes = taxableBase*this.taxPercentage;
        this.total = taxableBase+taxes;
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    private void registerEvent(DomainOrderEvent event) {
        this.domainEvents.add(event);
    }


    public List<DomainOrderEvent> pullDomainEvents() {
        List<DomainOrderEvent> copy = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return copy;
    }
}
