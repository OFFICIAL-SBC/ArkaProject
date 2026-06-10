package org.sebasbocruz.ms_orders.domain.context.orders.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.sebasbocruz.ms_orders.domain.commons.errors.InvalidStateTransitionException;
import org.sebasbocruz.ms_orders.domain.commons.states.Order.ShipmentStatus;
import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.WarehouseOrigin;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class Shipment {
    private final Long shipmentId;
    private final Long warehouseId;
    private final WarehouseOrigin origin;
    private final double distance;
    private Instant estimatedArrival;
    private ShipmentStatus status;
    private Instant dispatchedAt;
    private Instant deliveredAt;
    private Instant preparedAt;
    private final List<ShipmentItem> items;

    public Shipment(
            Long shipmentId,
            Long warehouseId,
            WarehouseOrigin origin,
            double distance,
            Instant estimatedArrival,
            List<ShipmentItem> items
    ){

        if(items.isEmpty()) throw new IllegalArgumentException("Empty Shipment");

        this.shipmentId = shipmentId;
        this.warehouseId = warehouseId;
        this.origin = origin;
        this.distance = distance;
        this.items = List.copyOf(items);
        this.status = ShipmentStatus.PREPARING;
        this.estimatedArrival = estimatedArrival;
    }

    public double subtotal(){
        return items.stream()
                .map( shipmentItem -> shipmentItem.quantity()*shipmentItem.unitPrice())
                .reduce(0.0,(a,b) -> a + b);
    }


    public void markAsDispatched(){
        if(this.status != ShipmentStatus.PREPARING){
            throw new InvalidStateTransitionException("Shipment",this.status.name(),ShipmentStatus.IN_TRANSIT.name(),"ms_order");
        }
        this.status = ShipmentStatus.IN_TRANSIT;
        this.dispatchedAt = Instant.now();
    }


    public void markAsPrepared(){
        if(this.status != ShipmentStatus.PENDING){
            throw new InvalidStateTransitionException("Shipment",this.status.name(),ShipmentStatus.PREPARING.name(),"ms_order");
        }

        this.status = ShipmentStatus.PREPARING;
        this.preparedAt = Instant.now();
    }


    void markAsDelivered() {
        if (status != ShipmentStatus.IN_TRANSIT)
            throw new InvalidStateTransitionException("Shipment",this.status.name(),ShipmentStatus.DELIVERED.name(),"ms_order");
        this.status = ShipmentStatus.DELIVERED;
        this.deliveredAt = Instant.now();
    }

}



