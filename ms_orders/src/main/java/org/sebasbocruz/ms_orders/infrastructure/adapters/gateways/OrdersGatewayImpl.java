package org.sebasbocruz.ms_orders.infrastructure.adapters.gateways;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import org.sebasbocruz.ms_orders.domain.context.orders.Gateways.OrdersGateway;
import org.sebasbocruz.ms_orders.infrastructure.adapters.Mappers.OrderMapper;
import org.sebasbocruz.ms_orders.infrastructure.adapters.repositories.CartDetailRepository;
import org.sebasbocruz.ms_orders.infrastructure.adapters.repositories.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class OrdersGatewayImpl implements OrdersGateway {

    private Logger logger = LoggerFactory.getLogger(OrdersGatewayImpl.class);
    private CartRepository cartRepository;
    private CartDetailRepository cartDetailRepository;

    private OrderMapper orderMapper;


    @Override
    public Mono<Order> createNewOrder(Long cartID) {
        return cartRepository.findById(cartID)
                .doOnSuccess(cartEntity -> logger.info("I am here"))
                .fromCallable(() -> orderMapper.fromInfrastructureToDomain());
    }
}
