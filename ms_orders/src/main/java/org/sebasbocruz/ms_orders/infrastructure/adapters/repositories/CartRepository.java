package org.sebasbocruz.ms_orders.infrastructure.adapters.repositories;

import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.CartEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CartRepository extends R2dbcRepository<CartEntity, Long> {
}

