package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.product.ProductEntity;

import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(schema = "cart", name = "cart_detail",
        uniqueConstraints = @UniqueConstraint(name = "uq_cart_product", columnNames = {"cart_id", "product_id"}))
public class CartDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_cartdetail_cart"))
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private CartEntity cartEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_cartdetail_product"))
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private ProductEntity product;

    @Column(nullable = false)
    private Integer amount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
