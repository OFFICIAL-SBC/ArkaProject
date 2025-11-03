package org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.mappers;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.AddressEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.InventoryEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.WarehouseEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.product.ProductEntity;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos.InventoryDTO;
import org.springframework.stereotype.Component;

@Component
public class QueryInventoryMapper {


    public InventoryDTO fromEntityToDTO(
            InventoryEntity inventoryEntity,
            ProductEntity productEntity,
            WarehouseEntity warehouseEntity,
            AddressEntity addressEntity
    ) {
        return new InventoryDTO(
                inventoryEntity.getInventoryId(),
                inventoryEntity.getProductId(),
                productEntity.getName(),
                inventoryEntity.getWarehouseId(),
                warehouseEntity.getName(),
                addressEntity.getAddress(),
                inventoryEntity.getAvailableStock(),
                inventoryEntity.getThresholdStock(),
                inventoryEntity.getCreatedAt(),
                inventoryEntity.getUpdatedAt()
        );
    }

}
