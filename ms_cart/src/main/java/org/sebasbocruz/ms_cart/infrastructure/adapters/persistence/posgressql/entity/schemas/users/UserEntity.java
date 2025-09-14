package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.users;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.sebasbocruz.ms_cart.domain.commons.enums.UserRole;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(schema = "users", name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "fk_user_client"))
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private ClientEntity clientEntity;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // DB type is PostgreSQL enum: user_role
    @Enumerated(EnumType.STRING)
    @Column(name = "role_user", nullable = false, columnDefinition = "user_role")
    private UserRole roleUser;

    @Column(length = 30)
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    // One user -> many carts
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<CartEntity> cartEntities = new ArrayList<>();
}