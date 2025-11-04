package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.publics;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.publics.CountryEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CountryRepositoryCommands extends R2dbcRepository<CountryEntity,Long> {
}
