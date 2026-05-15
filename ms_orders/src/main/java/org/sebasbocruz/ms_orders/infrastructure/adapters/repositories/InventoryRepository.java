package org.sebasbocruz.ms_orders.infrastructure.adapters.repositories;

import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Inventory.InventoryEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface InventoryRepository extends R2dbcRepository<InventoryEntity,Long> {
}
