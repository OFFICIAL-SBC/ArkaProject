package org.sebasbocruz.ms_orders.domain.context.orders.entity;

public record ShipmentItem (
        Long ShipmentItemId ,
        Long productId,// snapshot
        int quantity,
        double unitPrice
){}