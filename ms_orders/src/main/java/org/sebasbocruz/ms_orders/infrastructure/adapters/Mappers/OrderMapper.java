package org.sebasbocruz.ms_orders.infrastructure.adapters.Mappers;

import org.sebasbocruz.ms_orders.domain.commons.states.OrderState;
import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.CartEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order fromInfrastructureToDomain(OrderEntity orderEntity){
        return new Order(
                orderEntity.getOrder_id(),
                orderEntity.getClient_id(),
                orderEntity.getUser_id(),
                orderEntity.getWarehouse_id(),
                orderEntity.getCurrency_id(),
                OrderState.PENDING,
                orderEntity.getTotal_price(),
                orderEntity.getTaxes(),
                0.256,
                orderEntity.getDiscount(),
                orderEntity.getCreatedAt(),
                orderEntity.getUpdatedAt()
        );
    }

}
