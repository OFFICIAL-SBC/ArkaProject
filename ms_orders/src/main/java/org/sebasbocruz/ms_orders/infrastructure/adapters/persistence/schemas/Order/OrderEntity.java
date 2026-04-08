package org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="orders.orders")
public class OrderEntity {

    @Id
    @Column("order_id")
    private Long order_id;

    @Column("client_id")
    private Long client_id;

    @Column("user_id")
    private Long user_id;

    @Column("warehouse_id")
    private Long warehouse_id;

    @Column("order_state_id")
    private Long order_state_id;

    @Column("currency_id")
    private Long currency_id;

    @Column("total")
    private double total_price;


    @Column("taxes")
    private double taxes;


    @Column("discount")
    private double discount;

    @CreatedDate
    @Column("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private Instant updatedAt;


}
