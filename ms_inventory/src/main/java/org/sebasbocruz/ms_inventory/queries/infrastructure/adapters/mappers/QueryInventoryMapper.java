package org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.mappers;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.publics.AddressEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.InventoryEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.WarehouseEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.product.ProductEntity;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos.InventoryDTOquery;
import org.springframework.stereotype.Component;

@Component
public class QueryInventoryMapper {


    public InventoryDTOquery fromEntityToDTO(
            InventoryEntity inventoryEntity,
            ProductEntity productEntity,
            WarehouseEntity warehouseEntity,
            AddressEntity addressEntity
    ) {
        return new InventoryDTOquery(
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
