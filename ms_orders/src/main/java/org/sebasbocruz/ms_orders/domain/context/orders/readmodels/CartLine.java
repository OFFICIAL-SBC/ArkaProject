package org.sebasbocruz.ms_orders.domain.context.orders.readmodels;

/**
 * A single requested product line of a cart (product + quantity).
 */
public record CartLine(
        // ! With the productId we can get the ProductEntities
        // ! this entity has the price and with amount we can calculate the subtotal
        // ! for that product
        Long productId,
        int amount
) {}
