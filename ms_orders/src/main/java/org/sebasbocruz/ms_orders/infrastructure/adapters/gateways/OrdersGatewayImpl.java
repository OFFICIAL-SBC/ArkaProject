package org.sebasbocruz.ms_orders.infrastructure.adapters.gateways;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_orders.domain.commons.errors.DataAccessException;
import org.sebasbocruz.ms_orders.domain.commons.errors.EntityNotFoundException;
import org.sebasbocruz.ms_orders.domain.commons.states.OrderState;
import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import org.sebasbocruz.ms_orders.domain.context.orders.Gateways.OrdersGateway;
import org.sebasbocruz.ms_orders.infrastructure.adapters.Mappers.OrderMapper;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.CartDetailEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.CartEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.OrderEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Product.ProductEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.repositories.CartDetailRepository;
import org.sebasbocruz.ms_orders.infrastructure.adapters.repositories.CartRepository;
import org.sebasbocruz.ms_orders.infrastructure.adapters.repositories.OrderRepository;
import org.sebasbocruz.ms_orders.infrastructure.adapters.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OrdersGatewayImpl implements OrdersGateway {

    private Logger logger = LoggerFactory.getLogger(OrdersGatewayImpl.class);
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    private final OrderMapper orderMapper;

    private static final int CART_STATE_CONVERTED = 5;
    private static final long DEFAULT_WAREHOUSE_ID = 1L;
    private static final long INITIAL_ORDER_STATE_ID = 1L;

    @Override
    public Mono<Order> createNewOrder(Long cartID) {

        return findCartOrFail(cartID)
                .filter(cartEntity -> cartEntity.getCartStateID() == CART_STATE_CONVERTED)
                .switchIfEmpty(Mono.error(new IllegalStateException("The CART must be in state converted")))
                .flatMap(cartEntity -> buildOrderEntity(cartEntity, cartID))
                .flatMap(orderEntity -> orderRepository.save(orderEntity))
                .map(orderMapper::fromInfrastructureToDomain)
                .onErrorMap(
                        ex -> !(ex instanceof EntityNotFoundException) && !(ex instanceof IllegalStateException),
                        ex -> new DataAccessException("CREATE","Order",ex)
                );
    }

    private Mono<CartEntity> findCartOrFail(Long cartId){
        return cartRepository.findById(cartId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Cart",cartId.toString(),"Order")));
    }

    private Mono<OrderEntity> buildOrderEntity(CartEntity cart, Long cartId){
        return cartDetailRepository.findCartDetailEntitiesByCartID(cartId)
                .collectList()
                .flatMap(details -> loadProductsByID(details)
                        .map(result -> assembleOrder(cart, details, result)));

    }

    private Mono<Map<Long, ProductEntity>> loadProductsByID(List<CartDetailEntity> details){
        return Flux.fromIterable(details)
                .flatMap(detail -> productRepository.findById(detail.getProductID()))
                .collectMap(productEntity -> productEntity.getProductID());
    }

    private OrderEntity assembleOrder(CartEntity cart, List<CartDetailEntity> details,Map<Long, ProductEntity> productsByID){
        double totalPrice = details.stream().mapToDouble(detail -> detail.getAmount()*productsByID.get(detail.getProductID()).getPrice()*(1-productsByID.get(detail.getProductID()).getDiscount()))
                .sum();

        return OrderEntity.builder()
                .client_id(cart.getUserId())
                .user_id(cart.getUserId())
                .warehouse_id(DEFAULT_WAREHOUSE_ID)
                .order_state_id(INITIAL_ORDER_STATE_ID)
                .currency_id(cart.getCurrencyID())
                .total_price(totalPrice)
                .build();

    }
}
