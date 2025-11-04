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
@Table("product.category ")
public class CategoryEntity {
    @Id
    @Column("category_id")
    private Long categoryId;

    @Column("name")
    private String name;
}
