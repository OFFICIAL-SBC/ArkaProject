package org.sebasbocruz.ms_orders.domain.context.orders.readmodels;

/**
 * Pricing snapshot of a product at the moment the order is placed.
 * The net unit price is derived by the domain (price minus discount),
 * keeping the pricing rule out of the infrastructure layer.
 */
public record ProductSnapshot(
        Long productId,
        double price,
        double discount
) {
    public double unitPrice() {
        return price * (1 - discount / 100);
    }
}
