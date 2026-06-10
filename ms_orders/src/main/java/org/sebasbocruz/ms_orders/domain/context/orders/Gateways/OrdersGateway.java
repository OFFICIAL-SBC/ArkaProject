package org.sebasbocruz.ms_orders.domain.context.orders.Gateways;

import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import reactor.core.publisher.Mono;

/**
 * Write port for the Order aggregate.
 *
 * @implNote currencyId is passed alongside the aggregate because persistence
 * stores a numeric currency_id while the domain models Currency by code with
 * no lookup source available yet (see plan / TODO).
 */
public interface OrdersGateway {

    Mono<Order> save(Order order, Long currencyId);
}
