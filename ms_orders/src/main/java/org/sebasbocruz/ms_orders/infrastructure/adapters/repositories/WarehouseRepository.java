package org.sebasbocruz.ms_orders.infrastructure.adapters.repositories;

import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Inventory.WarehouseEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface WarehouseRepository extends R2dbcRepository<WarehouseEntity,Long> {
}
