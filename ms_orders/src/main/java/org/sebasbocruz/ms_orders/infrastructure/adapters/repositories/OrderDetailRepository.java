package org.sebasbocruz.ms_orders.infrastructure.adapters.repositories;

import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.OrderDetailEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrderDetailRepository extends R2dbcRepository<OrderDetailEntity, Long> {
}
