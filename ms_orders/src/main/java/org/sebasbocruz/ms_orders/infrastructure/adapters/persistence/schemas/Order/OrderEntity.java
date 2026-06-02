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

    @Column("order_state_id")
    private Long orderStateId;

    @Column("currency_id")
    private Long currencyId;

    @Column("total")
    private double totalPrice;

    @CreatedDate
    @Column("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private Instant updatedAt;


}
