package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.inventory;

import org.sebasbocruz.ms_inventory.commands.domain.commons.movement.MovementType;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.MovementEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface MovementRepositoryCommands extends R2dbcRepository<MovementEntity, Long> {

    Flux<MovementEntity> findMovementEntitiesByInventoryId(Long inventoryId);

    Flux<MovementEntity> findMovementEntitiesByInventoryIdAndMovementType(Long inventoryId, MovementType movementType);

}
