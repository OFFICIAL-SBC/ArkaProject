package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.product;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("product.product")
public class ProductEntity {

    @Id
    @Column("product_id")
    private Long productId;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("price")
    private double price;

    @Column("discount")
    private double discount;
}
