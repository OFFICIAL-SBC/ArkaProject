package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;

import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(schema = "cart", name = "cart_state")
public class CartStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_state_id")
    private Integer id;

    // DB type is PostgreSQL enum: cart_state_enum
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "cart_state", nullable = false, columnDefinition = "cart_state_enum")
    private CartState cartState;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}