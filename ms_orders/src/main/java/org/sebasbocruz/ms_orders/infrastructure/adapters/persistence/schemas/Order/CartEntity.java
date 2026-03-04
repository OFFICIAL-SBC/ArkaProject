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
@Table(schema="cart", name="cart")
public class CartEntity {

    @Id
    @Column("cart_id")
    private Long cartId;

    @Column("user_id")
    private Long userId;

    @Column("cart_state_id")
    private Long cartStateID;

    @Column("currency_id")
    private Long currencyID;

    @CreatedDate
    @Column("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private Instant updatedAt;

}
