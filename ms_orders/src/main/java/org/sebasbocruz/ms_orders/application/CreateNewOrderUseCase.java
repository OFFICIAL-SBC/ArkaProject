package org.sebasbocruz.ms_orders.application;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import org.sebasbocruz.ms_orders.domain.context.orders.Gateways.OrdersGateway;
import org.sebasbocruz.ms_orders.infrastructure.adapters.DTOS.OUT.OrderDTO;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateNewOrderUseCase {

    private final OrdersGateway ordersGateway;


    public Mono<OrderDTO> execute(Long cartID){

        return ordersGateway.createNewOrder(cartID)
                .map(newOrder -> new OrderDTO(newOrder.getOrderId()));
    }

}
