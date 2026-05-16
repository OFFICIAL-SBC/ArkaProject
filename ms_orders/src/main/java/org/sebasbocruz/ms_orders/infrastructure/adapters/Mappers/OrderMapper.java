package org.sebasbocruz.ms_orders.infrastructure.adapters.Mappers;

import org.sebasbocruz.ms_orders.domain.commons.states.Order.OrderState;

import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order fromInfrastructureToDomain(OrderEntity orderEntity, OrderState state){
        return new Order(
                orderEntity.getOrder_id(),
                orderEntity.getClient_id(),
                orderEntity.getUser_id(),
                orderEntity.getCurrency_id(),
                orderEntity.getOrder_state_id(),
                state,
                orderEntity.getTotal_price(),
                orderEntity.getCreatedAt(),
                orderEntity.getUpdatedAt()
        );
    }

}
