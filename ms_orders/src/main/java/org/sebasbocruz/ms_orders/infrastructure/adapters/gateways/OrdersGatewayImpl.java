package org.sebasbocruz.ms_orders.infrastructure.adapters.gateways;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_orders.domain.commons.errors.DataAccessException;
import org.sebasbocruz.ms_orders.domain.commons.errors.EntityNotFoundException;
import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import org.sebasbocruz.ms_orders.domain.context.orders.Gateways.OrdersGateway;
import org.sebasbocruz.ms_orders.infrastructure.adapters.Mappers.OrderMapper;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.OrderEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.repositories.CartDetailRepository;
import org.sebasbocruz.ms_orders.infrastructure.adapters.repositories.CartRepository;
import org.sebasbocruz.ms_orders.infrastructure.adapters.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class OrdersGatewayImpl implements OrdersGateway {

    private Logger logger = LoggerFactory.getLogger(OrdersGatewayImpl.class);
    private CartRepository cartRepository;
    private CartDetailRepository cartDetailRepository;
    private OrderRepository orderRepository;

    private OrderMapper orderMapper;


    @Override
    public Mono<Order> createNewOrder(Long cartID) {
        return cartRepository.findById(cartID)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Cart with ID "+cartID+"was not found")))
                .doOnNext(cartEntity ->logger.info("Cart found, creating order for cartId: {}", cartEntity.getCartId()))
                .flatMap(cartEntity -> {
                        return orderRepository.save(
                                OrderEntity.builder()
                                .client_id((long)1)
                                .user_id((long)1)
                                .warehouse_id((long)2)
                                .order_state_id((long)1)
                                .currency_id((long)1)
                                .total_price(856.36)
                                .taxes(0.01)
                                .discount(0)
                                .createdAt(Instant.now())
                                .updatedAt(Instant.now())
                                        .build()
                        );
                    }
                )
                .map(orderEntity -> {return orderMapper.fromInfrastructureToDomain(orderEntity);})
                .onErrorMap(
                        exception ->!(exception instanceof EntityNotFoundException),
                        exception -> new DataAccessException("Error trying to get CartByID", exception)
                );
    }
}
