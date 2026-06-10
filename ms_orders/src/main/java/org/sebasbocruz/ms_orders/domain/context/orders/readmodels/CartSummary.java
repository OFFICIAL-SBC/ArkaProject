package org.sebasbocruz.ms_orders.domain.context.orders.readmodels;

/**
 * Minimal projection of a cart needed to provision an order.
 * Keeps the application layer from depending on the infrastructure CartEntity.
 */
public record CartSummary(
        Long cartId,
        Long userId,
        Long cartStateId,
        Long currencyId
) {}
