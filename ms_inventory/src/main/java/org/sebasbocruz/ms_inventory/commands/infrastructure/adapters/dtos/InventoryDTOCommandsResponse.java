package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class InventoryDTOCommandsResponse {
    private Long warehouseId;
    private String warehouseName;
    private String warehouseAddress;
    private String cityName;
    private String countryName;
    private Long productId;
    private String productName;
    private int initialStock;
    private int thresholdStock;
}
