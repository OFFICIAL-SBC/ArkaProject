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
@Table(schema = "orders",name="order_state")
public class OrderStateEntity {

    @Id
    @Column("order_state_id")
    private Long orderSateId;

    @Column("state")
    private String orderState;

}
