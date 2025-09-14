package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.users;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(schema = "users", name = "client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @Column(nullable = false, length = 30)
    private String nit;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(name = "email_billing", nullable = false, length = 30)
    private String emailBilling;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "clientEntity", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<UserEntity> userEntities = new ArrayList<>();
}
