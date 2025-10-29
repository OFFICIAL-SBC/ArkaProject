package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("inventory.inventory")
public class InventoryEntity {

    @Id
    @Column("inventory_id")
    private Long inventoryId;

    @Column("product_id")
    private Long productId;

    @Column("warehouse_id")
    private Long warehouseId;

    @Column("available_stock")
    private Integer availableStock;

    @Column("threshold_stock")
    private Integer thresholdStock;

    @CreatedDate
    @Column("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private Instant updatedAt;

}
