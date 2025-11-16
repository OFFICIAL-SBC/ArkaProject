package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.inventory;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.InventoryEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface InventoryRepositoryCommands extends R2dbcRepository<InventoryEntity, Long> {

    Mono<InventoryEntity> findInventoryEntityByProductId(Long productId);

}
