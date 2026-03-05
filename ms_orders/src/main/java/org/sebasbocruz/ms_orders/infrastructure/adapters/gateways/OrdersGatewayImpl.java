package org.sebasbocruz.ms_orders.infrastructure.adapters.gateways;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_orders.domain.commons.errors.DataAccessException;
import org.sebasbocruz.ms_orders.domain.commons.errors.EntityNotFoundException;
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
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Cart with ID "+cartID+"was not found")))
                .doOnNext(cartEntity ->logger.info("Cart found, creating order for cartId: {}", cartEntity.getCartId()))
                .map(cartEntity -> {return orderMapper.fromInfrastructureToDomain(cartEntity);})
                .onErrorMap(
                        exception ->!(exception instanceof EntityNotFoundException),
                        exception -> new DataAccessException("Error trying to get CartByID", exception)
                );
    }
}
