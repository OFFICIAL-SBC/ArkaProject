package org.sebasbocruz.ms_orders.infrastructure.adapters.gateways;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_orders.domain.commons.errors.DataAccessException;
import org.sebasbocruz.ms_orders.domain.commons.errors.EntityNotFoundException;
import org.sebasbocruz.ms_orders.domain.commons.errors.InvalidStateTransitionException;
import org.sebasbocruz.ms_orders.domain.commons.states.Cart.CartState;
import org.sebasbocruz.ms_orders.domain.commons.states.Order.OrderState;
import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import org.sebasbocruz.ms_orders.domain.context.orders.Gateways.OrdersGateway;
import org.sebasbocruz.ms_orders.infrastructure.adapters.Mappers.OrderMapper;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Cart.CartDetailEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Cart.CartEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Inventory.WarehouseEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Order.OrderEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Product.ProductEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.publics.AddressEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.user.UserEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
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
    private final InventoryRepository inventoryRepository;
    private final OrderStateRepository orderStateRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final WarehouseRepository warehouseRepository;

    private final OrderMapper orderMapper;

    private static final int CART_STATE_CONVERTED = 5;
    private static final long ORDER_STATE_PENDING = 1L;

    @Override
    public Mono<Order> createNewOrder(Long cartID) {
        return findCartOrFail(cartID)
                .flatMap(this::ensureCartStateIsConverted)
                .flatMap(this::buildOrderEntity)
                .flatMap(orderRepository::save)
                .flatMap(this::buildDomainOrderSavedWithStatus)
                .onErrorMap(
                        ex -> !(ex instanceof EntityNotFoundException) && !(ex instanceof InvalidStateTransitionException),
                        ex -> new DataAccessException("CREATE","Order",ex)
                );
    }

    private Mono<CartEntity> findCartOrFail(Long cartId){
        return cartRepository.findById(cartId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Cart",cartId.toString(),"Order")));
    }

    private Mono<CartEntity> ensureCartStateIsConverted(CartEntity cart){
        if(cart.getCartStateID() == CART_STATE_CONVERTED){
            return Mono.just(cart);
        }

        return Mono.error(
                new InvalidStateTransitionException(
                        "Cart",
                        CartState.CART_STATES.get(cart.getCartStateID().intValue()),
                        "Order",
                        "Order"
                )
        );
    }

    private Mono<OrderEntity> buildOrderEntity(CartEntity cart){

        Mono<AddressEntity> userAddressEntity = getUserEntity(cart.getUserId())
                .flatMap(user -> getAddressById(user.getAddressID()));

        return cartDetailRepository.findCartDetailEntitiesByCartID(cart.getCartId())
                .collectList()
                .flatMap(details -> loadProductsByID(details)
                        .map(productMap -> assembleOrder(cart, details, productMap)));

    }

    private Mono<UserEntity> getUserEntity(Long userID){
        return userRepository.findById(userID)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User",userID.toString(),"Order")));
    }

    private Mono<AddressEntity> getAddressById(Long addressId){
        return addressRepository.findById(addressId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Address",addressId.toString(),"Order")));
    }

    private Mono<WarehouseEntity> getWarehouseByID(Long warehouseId){
        return warehouseRepository.findById(warehouseId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Warehouse",warehouseId.toString(),"Orders")));
    }

    private Mono<Map<Long, ProductEntity>> loadProductsByID(List<CartDetailEntity> details){
        return Flux.fromIterable(details)
                .flatMap(detail -> productRepository.findById(detail.getProductID()))
                .collectMap(productEntity -> productEntity.getProductID());
    }

    private Mono<Map<Long, ArrayList<ProductEntity>>> groupProductsByWarehouse(List<CartDetailEntity> details){
        return Flux.fromIterable(details)
                .flatMap(cartDetail -> inventoryRepository.findInventoryEntitiesByProductId(cartDetail.getProductID()))
                .flatMap(detail -> productRepository.findById(detail.getProductID()))

    }

    private OrderEntity assembleOrder(CartEntity cart, List<CartDetailEntity> details,Map<Long, ProductEntity> productsByID){
        double totalPrice = details.stream().mapToDouble(detail -> detail.getAmount()*productsByID.get(detail.getProductID()).getPrice()*(1-productsByID.get(detail.getProductID()).getDiscount()/100))
                .sum();

        return OrderEntity.builder()
                .client_id(cart.getUserId())
                .user_id(cart.getUserId())
                .currencyId(cart.getCurrencyID())
                .orderStateId(ORDER_STATE_PENDING)
                .totalPrice(totalPrice)
                .build();

    }

    private Mono<Order> buildDomainOrderSavedWithStatus(OrderEntity orderSaved){

        return orderStateRepository.findById(orderSaved.getOrderStateId())
                .map(orderStateEntity -> orderMapper.fromInfrastructureToDomain(orderSaved,OrderState.valueOf(orderStateEntity.getOrderState())));

    }
}
