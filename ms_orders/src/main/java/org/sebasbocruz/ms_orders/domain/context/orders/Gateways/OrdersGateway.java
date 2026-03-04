package org.sebasbocruz.ms_orders.domain.context.orders.Gateways;

import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import reactor.core.publisher.Mono;

public interface OrdersGateway {

    Mono<Order> createNewOrder(Long cartID);
}
