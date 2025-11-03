package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.Mappers;

import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate.Inventory;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.ValueObjects.Quantity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.InventoryEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public final class CommandsInventoryMapper {

    private final Logger logger = LoggerFactory.getLogger(CommandsInventoryMapper.class);

    private CommandsInventoryMapper() {}

    // 🧩 Infra → Domain
    public  Inventory fromEntityToDomain(InventoryEntity entity) {
        if (entity == null) return null;


        return new Inventory(
                entity.getInventoryId(),
                entity.getProductId(),
                entity.getWarehouseId(),
                new Quantity(entity.getAvailableStock()),
                new Quantity(entity.getThresholdStock()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // 🔁 Domain → Infra
    public  InventoryEntity FromDomainToEntity(Inventory domain) {
        if (domain == null) return null;

        return new InventoryEntity(
                domain.getId(),
                domain.getProductId(),
                domain.getWarehouseId(),
                domain.getAvailable().value(),
                domain.getThreshold().value(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}

