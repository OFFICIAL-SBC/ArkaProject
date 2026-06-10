package org.sebasbocruz.ms_orders.application;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_orders.domain.commons.errors.DataAccessException;
import org.sebasbocruz.ms_orders.domain.commons.errors.EntityNotFoundException;
import org.sebasbocruz.ms_orders.domain.commons.errors.InvalidStateTransitionException;
import org.sebasbocruz.ms_orders.domain.commons.states.Cart.CartState;
import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import org.sebasbocruz.ms_orders.domain.context.orders.Gateways.OrderProvisioningGateway;
import org.sebasbocruz.ms_orders.domain.context.orders.Gateways.OrdersGateway;
import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.Currency;
import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.DeliveryAddress;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.CartLine;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.CartSummary;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.ProductSnapshot;
import org.sebasbocruz.ms_orders.domain.context.orders.services.ShipmentFactory;
import org.sebasbocruz.ms_orders.domain.context.orders.services.WarehouseSelectionService;
import org.sebasbocruz.ms_orders.infrastructure.adapters.DTOS.OUT.OrderDTO;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Orchestrates the creation of an order from a converted cart: pulls the data
 * it needs through ports, runs the domain rules (cart-state validation, closest
 * warehouse selection, shipment assembly), builds the Order aggregate and saves
 * it. All persistence and querying lives behind the gateways.
 */
@RequiredArgsConstructor
public class CreateNewOrderUseCase {

    private static final int CART_STATE_CONVERTED = 5;

    private final OrdersGateway ordersGateway;
    private final OrderProvisioningGateway provisioning;
    private final Clock clock;

    public Mono<OrderDTO> execute(Long cartID) {
        return provisioning.findCart(cartID)
                .flatMap(this::ensureCartStateIsConverted)
                .flatMap(this::buildAndSaveOrder)
                .map(order -> new OrderDTO(order.getOrderId()))
                .onErrorMap(
                        ex -> !(ex instanceof EntityNotFoundException) && !(ex instanceof InvalidStateTransitionException),
                        ex -> new DataAccessException("CREATE", "Order", ex)
                );
    }

    private Mono<CartSummary> ensureCartStateIsConverted(CartSummary cart) {
        if (cart.cartStateId() != null && cart.cartStateId() == CART_STATE_CONVERTED) {
            return Mono.just(cart);
        }
        return Mono.error(new InvalidStateTransitionException(
                "Cart",
                CartState.CART_STATES.get(cart.cartStateId().intValue()),
                "Order",
                "Order"
        ));
    }

    private Mono<Order> buildAndSaveOrder(CartSummary cart) {
        return provisioning.findCartLines(cart.cartId()).collectList()
                .flatMap(lines -> {
                    Set<Long> productIds = lines.stream()
                            .map(CartLine::productId)
                            .collect(Collectors.toSet());
                    Map<Long, CartLine> linesByProduct = lines.stream()
                            .collect(Collectors.toMap(CartLine::productId, Function.identity()));

                    return Mono.zip(
                            provisioning.findClientId(cart.userId()),
                            provisioning.findDeliveryAddress(cart.userId()),
                            provisioning.findProductSnapshots(productIds)
                                    .collectMap(ProductSnapshot::productId)
                    ).flatMap(data -> {
                        Long clientId = data.getT1();
                        DeliveryAddress address = data.getT2();
                        Map<Long, ProductSnapshot> snapshots = data.getT3();

                        return provisioning.findWarehouseCandidates(productIds, address.coordinates())
                                .collectList()
                                .map(WarehouseSelectionService::pickClosestPerProduct)
                                .map(selection -> ShipmentFactory.build(selection, linesByProduct, snapshots, clock))
                                .map(shipments -> Order.place(
                                        null,
                                        clientId,
                                        cart.userId(),
                                        address,
                                        shipments,
                                        // TODO: resolve a real ISO currency code from currency_id once a source exists.
                                        new Currency(String.valueOf(cart.currencyId())),
                                        clock
                                ))
                                .flatMap(order -> ordersGateway.save(order, cart.currencyId()));
                    });
                });
    }
}
