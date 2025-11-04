package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class InventoryDTOcommands {
    private Long warehouseId;
    private Long brandId;
    private Long categoryId;
    private String name;
    private String description;
    private double price;
    private double discount;
    private int initialStock;
    private int thresholdStock;
}
