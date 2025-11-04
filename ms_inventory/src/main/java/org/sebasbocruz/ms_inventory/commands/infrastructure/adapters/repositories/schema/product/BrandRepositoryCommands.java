package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.product;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.product.BrandEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BrandRepositoryCommands extends R2dbcRepository<BrandEntity, Long> {
}
