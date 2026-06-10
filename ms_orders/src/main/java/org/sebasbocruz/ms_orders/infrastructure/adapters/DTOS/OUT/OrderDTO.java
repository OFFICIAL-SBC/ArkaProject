package org.sebasbocruz.ms_orders.infrastructure.adapters.DTOS.OUT;

/**
 * Minimal response returned when a new order is created.
 */
public record OrderDTO(
        Long orderId
) {}
