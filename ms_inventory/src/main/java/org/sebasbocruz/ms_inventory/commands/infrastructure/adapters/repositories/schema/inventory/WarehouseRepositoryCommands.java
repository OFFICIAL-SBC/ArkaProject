package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.inventory;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.WarehouseEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface WarehouseRepositoryCommands extends R2dbcRepository<WarehouseEntity,Long> {
}
