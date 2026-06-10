package org.sebasbocruz.ms_orders.domain.context.orders.services;

import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.ProductWarehouseCandidate;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Picks, for each product, the warehouse with the lowest distance to the
 * delivery destination. Pure function — easy to unit test, no I/O.
 */
public final class WarehouseSelectionService {

    private WarehouseSelectionService() {}

    public static Map<Long, ProductWarehouseCandidate> pickClosestPerProduct(
            List<ProductWarehouseCandidate> candidates) {

        return candidates.stream()
                .collect(Collectors.toMap(
                        ProductWarehouseCandidate::productId,
                        candidate -> candidate,
                        closest()
                ));
    }

    private static BinaryOperator<ProductWarehouseCandidate> closest() {
        return (a, b) -> a.distance() <= b.distance() ? a : b;
    }
}
