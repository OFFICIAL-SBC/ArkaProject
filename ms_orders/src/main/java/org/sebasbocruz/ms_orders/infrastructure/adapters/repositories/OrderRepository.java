package org.sebasbocruz.ms_orders.infrastructure.adapters.repositories;


import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.OrderEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrderRepository extends R2dbcRepository<OrderEntity, Long> {
}
