package org.sebasbocruz.ms_orders.application;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import org.sebasbocruz.ms_orders.domain.context.orders.Gateways.OrdersGateway;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateNewOrderUseCase {

    private final OrdersGateway ordersGateway;


    public Mono<Order> execute(Long cartID){
        return ordersGateway.createNewOrder(cartID);
    }

}
