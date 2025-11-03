package org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class InventoryDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Long warehouseId;
    private String warehouseName;
    private String warehouseAddress;
    private Integer availableStock;
    private Integer thresholdStock;
    private Instant createdAt;
    private Instant updatedAt;

}
