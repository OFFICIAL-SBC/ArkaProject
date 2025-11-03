package org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.repositories;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.product.ProductEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface QueryProductRepository extends R2dbcRepository<ProductEntity, Long> {
}
