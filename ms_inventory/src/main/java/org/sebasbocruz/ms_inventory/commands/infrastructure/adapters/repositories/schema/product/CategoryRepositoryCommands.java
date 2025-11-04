package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.product;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.product.CategoryEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CategoryRepositoryCommands extends R2dbcRepository<CategoryEntity, Long> {
}
