package org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(schema = "inventory",name="inventory")
public class InventoryEntity {

    @Id
    @Column("inventory_id")
    private Long inventoryId;

    @Column("product_id")
    private Long productId;

    @Column("warehouse_id")
    private Long warehouseId;

}
