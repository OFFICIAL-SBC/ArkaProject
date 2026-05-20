package org.sebasbocruz.ms_orders.domain.context.orders.entity;

import java.time.Instant;

public class Shipment {
    private final Long shipmentId;
    private final Long warehouseId;
    private final WarehouseOrigin origin;
    private final Distance distance;
    private Instant estimatedArrival;
    private ShipmentStatus status;
    private final List<ShipmentItem> items;

    public Shipment(
            Long shipmentId,
            Long warehouseId,
            WarehouseOrigin origin,
            Distance distance,
            Instant estimatedArrival,
            List<ShipmentItem> items){
        if(items.isEmpty()) throw IllegalArgumentException("Empty Shipment");
        this.shipmentId = shipmentId;
        this.warehouseId = warehouseId;

    }

}
