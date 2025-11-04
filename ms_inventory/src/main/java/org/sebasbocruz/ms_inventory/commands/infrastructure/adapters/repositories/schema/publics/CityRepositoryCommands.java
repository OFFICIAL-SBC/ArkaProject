package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.publics;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.publics.CityEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CityRepositoryCommands extends R2dbcRepository<CityEntity, Long> {
}
