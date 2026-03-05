package org.sebasbocruz.ms_orders.infrastructure.adapters.Mappers;

import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.CartEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {


    public Order fromInfrastructureToDomain(CartEntity cartEntity){
        return new Order();
    }

}
