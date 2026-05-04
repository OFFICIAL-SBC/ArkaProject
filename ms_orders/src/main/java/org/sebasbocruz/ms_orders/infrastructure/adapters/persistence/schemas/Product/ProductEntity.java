package org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Product;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema="product",name="product")
public class ProductEntity {

    @Id
    @Column("product_id")
    private Long productID;

    @Column("brand_id")
    private Long brandID;

    @Column("category_id")
    private Long categoryID;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("price")
    private double price;

    @Column("discount")
    private double discount;

}
