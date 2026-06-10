package org.sebasbocruz.ms_orders.domain.context.orders.readmodels;

/**
 * A single requested product line of a cart (product + quantity).
 */
public record CartLine(
        Long productId,
        int amount
) {}
