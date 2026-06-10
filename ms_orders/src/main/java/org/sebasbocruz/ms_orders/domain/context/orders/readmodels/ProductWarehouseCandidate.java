package org.sebasbocruz.ms_orders.domain.context.orders.readmodels;

import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.WarehouseOrigin;

/**
 * A warehouse that can fulfil a given product, together with its distance
 * (already computed by infrastructure) to the delivery destination.
 * The domain only has to pick the closest candidate per product.
 */
public record ProductWarehouseCandidate(
        Long productId,
        Long warehouseId,
        WarehouseOrigin origin,
        double distance
) {}
