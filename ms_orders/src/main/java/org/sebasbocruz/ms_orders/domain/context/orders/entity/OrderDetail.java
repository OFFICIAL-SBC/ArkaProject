package org.sebasbocruz.ms_orders.domain.context.orders.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDetail {

    private final Long orderDetailId; // can be null before persistence
    private final Long productId;
    private final int amount;
    private final double unitPrice;

    public OrderDetail(Long orderDetailId, Long productId, int amount, double unitPrice) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price must be >= 0");
        }
        this.orderDetailId = orderDetailId;
        this.productId = productId;
        this.amount = amount;
        this.unitPrice = unitPrice;
    }

    public double getLineTotal() {
        return unitPrice*amount;
    }
}
