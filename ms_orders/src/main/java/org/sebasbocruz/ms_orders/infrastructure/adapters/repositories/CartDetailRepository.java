package org.sebasbocruz.ms_orders.infrastructure.adapters.repositories;

import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.CartDetailEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface CartDetailRepository extends R2dbcRepository<CartDetailEntity,Long> {
    Flux<CartDetailEntity> findCartDetailEntitiesByCartID(Long cartID);
}
