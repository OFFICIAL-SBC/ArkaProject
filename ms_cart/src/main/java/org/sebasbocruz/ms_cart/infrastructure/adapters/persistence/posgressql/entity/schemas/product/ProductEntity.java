package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.product;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(schema = "product", name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    // add other columns if you have them (brand_id, category_id, etc.)
}
