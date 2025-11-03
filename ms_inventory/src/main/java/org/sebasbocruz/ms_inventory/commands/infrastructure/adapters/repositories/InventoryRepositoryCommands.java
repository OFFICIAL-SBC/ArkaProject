package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.InventoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface InventoryRepositoryCommands extends R2dbcRepository<InventoryEntity, Long> {



}
