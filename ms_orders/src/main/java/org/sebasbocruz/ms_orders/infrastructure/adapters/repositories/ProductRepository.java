package org.sebasbocruz.ms_orders.infrastructure.adapters.repositories;

import org.reactivestreams.Publisher;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Product.ProductEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends R2dbcRepository<ProductEntity, Long> {

}
