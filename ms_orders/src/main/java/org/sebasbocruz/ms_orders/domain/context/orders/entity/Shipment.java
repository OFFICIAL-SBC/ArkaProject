package org.sebasbocruz.ms_orders.domain.context.orders.entity;

import java.time.Instant;
import java.util.List;

public class Shipment {
    private final Long shipmentId;
    private final Long warehouseId;
    private final WarehouseOrigin origin;
    private final double distance;
    private Instant estimatedArrival;
    private ShipmentStatus status;
    private final List<ShipmentItem> items;

    public Shipment(
            Long shipmentId,
            Long warehouseId,
            WarehouseOrigin origin,
            double distance,
            Instant estimatedArrival,
            List<ShipmentItem> items){

        if(items.isEmpty()) throw IllegalArgumentException("Empty Shipment");
        this.shipmentId = shipmentId;
        this.warehouseId = warehouseId;
    }

}


record ShipmentItem (
     Long id,
     Long productId,// snapshot
     int quantity,
     double unitPrice
){}


record WarehouseOrigin(String displayName, String city, String country) {}

 enum ShipmentStatus { PREPARING, IN_TRANSIT, DELIVERED, CANCELLED }
