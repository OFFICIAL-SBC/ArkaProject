package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.common.CurrencyEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.users.UserEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(schema = "cart", name = "cart")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_cart_user"))
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_state_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_cart_state"))
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private CartStateEntity cartState;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_cart_currency"))
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private CurrencyEntity currencyEntity;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<CartDetailEntity> details = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
