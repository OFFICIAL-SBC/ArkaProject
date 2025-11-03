package org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.repositories;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.WarehouseEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface QueryWarehouseRepository extends R2dbcRepository<WarehouseEntity, Long> {
}
