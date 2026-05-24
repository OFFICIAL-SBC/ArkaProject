package org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects;

public record WarehouseOrigin(
        String displayName,
        String city,
        String country
) {}