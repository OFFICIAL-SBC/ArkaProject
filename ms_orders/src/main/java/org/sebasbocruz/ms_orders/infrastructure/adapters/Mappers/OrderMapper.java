package org.sebasbocruz.ms_orders.infrastructure.adapters.Mappers;

import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import org.sebasbocruz.ms_orders.domain.context.orders.entity.Shipment;
import org.sebasbocruz.ms_orders.domain.context.orders.entity.ShipmentItem;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.OrderDetailEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.OrderEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    private static final long ORDER_STATE_PENDING = 1L;

    /**
     * Maps the order header. The id is left null so R2DBC performs an insert;
     * currencyId is supplied separately since the domain models currency by code.
     */
    public OrderEntity toEntity(Order order, Long currencyId) {
        return OrderEntity.builder()
                .order_id(order.getOrderId())
                .client_id(order.getClientId())
                .user_id(order.getUserId())
                .orderStateId(ORDER_STATE_PENDING)
                .currencyId(currencyId)
                .totalPrice(order.getTotal())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    /**
     * Flattens the aggregate's shipments into order_detail rows, one per item.
     * The warehouse_id column preserves which warehouse each line ships from.
     */
    public List<OrderDetailEntity> toDetailEntities(Long orderId, Order order) {
        List<OrderDetailEntity> details = new ArrayList<>();
        for (Shipment shipment : order.getShipments()) {
            for (ShipmentItem item : shipment.getItems()) {
                details.add(OrderDetailEntity.builder()
                        .orderId(orderId)
                        .productId(item.productId())
                        .warehouseId(shipment.getWarehouseId())
                        .productUnits(item.quantity())
                        .totalValue(item.quantity() * item.unitPrice())
                        .build());
            }
        }
        return details;
    }
}
