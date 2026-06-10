package org.sebasbocruz.ms_orders.domain.context.orders.Gateways;

import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.Coordinates;
import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.DeliveryAddress;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.CartLine;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.CartSummary;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.ProductSnapshot;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.ProductWarehouseCandidate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Read port that gathers every piece of data the use case needs to provision
 * an order. All methods are pure I/O; no business decisions live here.
 */
public interface OrderProvisioningGateway {

    Mono<CartSummary> findCart(Long cartId);

    Flux<CartLine> findCartLines(Long cartId);

    Mono<Long> findClientId(Long userId);

    Mono<DeliveryAddress> findDeliveryAddress(Long userId);

    Flux<ProductSnapshot> findProductSnapshots(Set<Long> productIds);

    /**
     * Every warehouse able to fulfil each product, with its distance to the
     * destination already computed by infrastructure.
     */
    Flux<ProductWarehouseCandidate> findWarehouseCandidates(Set<Long> productIds, Coordinates destination);
}
