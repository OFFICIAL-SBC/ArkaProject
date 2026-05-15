package org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(schema = "orders",name = "order_detail")
public class OrderDetailEntity {

    @Id
    @Column("order_detail_id")
    private Long orderDetailId;

    @Column("order_id")
    private Long orderId;

    @Column("product_id")
    private Long productId;

    @Column("warehouse_id")
    private Long warehouseId;

    @Column("units")
    private Integer productUnits;

    @Column("total_value")
    private Double totalValue;

}
