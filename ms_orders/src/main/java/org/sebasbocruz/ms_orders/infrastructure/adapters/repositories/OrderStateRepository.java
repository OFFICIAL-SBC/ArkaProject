package org.sebasbocruz.ms_orders.infrastructure.adapters.repositories;


import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.OrderStateEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrderStateRepository  extends R2dbcRepository<OrderStateEntity,Long> {
}
