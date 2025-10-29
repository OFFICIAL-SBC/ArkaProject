package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.Mappers;

import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate.Inventory;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.ValueObjects.Quantity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.InventoryEntity;

public final class InventoryMapper {

    private InventoryMapper() {}

    // 🧩 Infra → Domain
    public static Inventory toDomain(InventoryEntity entity) {
        if (entity == null) return null;

        return new Inventory(
                entity.getInventoryId(),
                entity.getProductId(),
                entity.getWarehouseId(),
                new Quantity(entity.getAvailableStock()),
                entity.getThresholdStock(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // 🔁 Domain → Infra
    public static InventoryEntity toEntity(Inventory domain) {
        if (domain == null) return null;

        return new InventoryEntity(
                domain.getId(),
                domain.getProductId(),
                domain.getWarehouseId(),
                domain.getAvailable().value(),
                domain.getThreshold(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}

