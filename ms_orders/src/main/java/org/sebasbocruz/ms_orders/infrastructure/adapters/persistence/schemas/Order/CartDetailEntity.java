package org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "cart", name="cart_detail")
public class CartDetailEntity {

    @Id
    @Column("cart_detail_id")
    private Long cartDetailID;

    @Column("cart_id")
    private Long cartID;

    @Column("product_id")
    private Long productID;

    @Column("amount")
    private int amount;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

}
