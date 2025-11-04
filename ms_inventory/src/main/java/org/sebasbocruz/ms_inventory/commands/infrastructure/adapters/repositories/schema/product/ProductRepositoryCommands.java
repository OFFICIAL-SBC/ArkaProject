package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.product;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.product.ProductEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ProductRepositoryCommands extends R2dbcRepository<ProductEntity, Long> {
    Mono<Boolean> existsByName(String name);
    
}
