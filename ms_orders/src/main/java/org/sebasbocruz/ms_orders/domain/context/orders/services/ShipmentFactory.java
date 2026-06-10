package org.sebasbocruz.ms_orders.domain.context.orders.services;

import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.WarehouseOrigin;
import org.sebasbocruz.ms_orders.domain.context.orders.entity.Shipment;
import org.sebasbocruz.ms_orders.domain.context.orders.entity.ShipmentItem;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.CartLine;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.ProductSnapshot;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.ProductWarehouseCandidate;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Assembles the shipments of an order from the per-product warehouse selection.
 * Products served by the same warehouse are grouped into a single shipment.
 * Pure function — no I/O.
 */
public final class ShipmentFactory {

    // TODO: replace with a real delivery rule (e.g. distance-based) once defined.
    private static final Duration ESTIMATED_LEAD_TIME = Duration.ofDays(2);

    private ShipmentFactory() {}

    public static List<Shipment> build(
            Map<Long, ProductWarehouseCandidate> selectionByProduct,
            Map<Long, CartLine> linesByProduct,
            Map<Long, ProductSnapshot> snapshotsByProduct,
            Clock clock) {

        Instant estimatedArrival = clock.instant().plus(ESTIMATED_LEAD_TIME);

        Map<Long, List<ProductWarehouseCandidate>> byWarehouse = new LinkedHashMap<>();
        for (ProductWarehouseCandidate candidate : selectionByProduct.values()) {
            byWarehouse.computeIfAbsent(candidate.warehouseId(), id -> new ArrayList<>()).add(candidate);
        }

        List<Shipment> shipments = new ArrayList<>();
        for (Map.Entry<Long, List<ProductWarehouseCandidate>> entry : byWarehouse.entrySet()) {
            List<ProductWarehouseCandidate> productsHere = entry.getValue();

            List<ShipmentItem> items = productsHere.stream()
                    .map(candidate -> toShipmentItem(candidate, linesByProduct, snapshotsByProduct))
                    .toList();

            ProductWarehouseCandidate any = productsHere.getFirst();
            shipments.add(new Shipment(
                    null,
                    entry.getKey(),
                    originOf(any),
                    any.distance(),
                    estimatedArrival,
                    items
            ));
        }

        return shipments;
    }

    private static ShipmentItem toShipmentItem(
            ProductWarehouseCandidate candidate,
            Map<Long, CartLine> linesByProduct,
            Map<Long, ProductSnapshot> snapshotsByProduct) {

        Long productId = candidate.productId();
        CartLine line = linesByProduct.get(productId);
        ProductSnapshot snapshot = snapshotsByProduct.get(productId);
        return new ShipmentItem(null, productId, line.amount(), snapshot.unitPrice());
    }

    private static WarehouseOrigin originOf(ProductWarehouseCandidate candidate) {
        return candidate.origin();
    }
}
