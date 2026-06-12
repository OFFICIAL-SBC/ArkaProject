package org.sebasbocruz.ms_orders.infrastructure.adapters.gateways;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_orders.domain.commons.errors.EntityNotFoundException;
import org.sebasbocruz.ms_orders.domain.context.orders.Aggregate.Order;
import org.sebasbocruz.ms_orders.domain.context.orders.Gateways.OrderProvisioningGateway;
import org.sebasbocruz.ms_orders.domain.context.orders.Gateways.OrdersGateway;
import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.Coordinates;
import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.DeliveryAddress;
import org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects.WarehouseOrigin;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.CartLine;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.CartSummary;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.ProductSnapshot;
import org.sebasbocruz.ms_orders.domain.context.orders.readmodels.ProductWarehouseCandidate;
import org.sebasbocruz.ms_orders.infrastructure.adapters.Mappers.OrderMapper;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Cart.CartEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.publics.AddressEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.publics.CityEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.publics.CountryEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.user.UserEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Inventory.WarehouseEntity;
import org.sebasbocruz.ms_orders.infrastructure.adapters.repositories.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Single adapter that implements both order ports. Pure I/O: it queries the
 * repositories and persists the aggregate. No business decisions live here —
 * those moved to the use case and the domain services.
 */
@RequiredArgsConstructor
@Service
public class OrdersGatewayImpl implements OrdersGateway, OrderProvisioningGateway {

    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final WarehouseRepository warehouseRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    private final OrderMapper orderMapper;

    // ---------------------------------------------------------------------
    // Read port (OrderProvisioningGateway)
    // ---------------------------------------------------------------------

    @Override
    public Mono<CartSummary> findCart(Long cartId) {
        return cartRepository.findById(cartId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Cart", cartId.toString(), "Order")))
                .map(cart -> new CartSummary(
                        cart.getCartId(),
                        cart.getUserId(),
                        cart.getCartStateID(),
                        cart.getCurrencyID()
                ));
    }

    @Override
    public Flux<CartLine> findCartLines(Long cartId) {
        return cartDetailRepository.findCartDetailEntitiesByCartID(cartId)
                .map(detail -> new CartLine(detail.getProductID(), detail.getAmount()));
    }

    @Override
    public Mono<Long> findClientId(Long userId) {
        return getUserEntity(userId).map(UserEntity::getClientID);
    }

    @Override
    public Mono<DeliveryAddress> findDeliveryAddress(Long userId) {
        return getUserEntity(userId)
                .flatMap(this::buildDeliveryAddressFromUser);
    }

    @Override
    public Flux<ProductSnapshot> findProductSnapshots(Set<Long> productIds) {
        return Flux.fromIterable(productIds)
                .flatMap(productRepository::findById)
                .map(product -> new ProductSnapshot(
                        product.getProductID(),
                        product.getPrice(),
                        product.getDiscount()
                ));
    }

    @Override
    public Flux<ProductWarehouseCandidate> findWarehouseCandidates(Set<Long> productIds, Coordinates destination) {
        double destLat = destination.latitude().doubleValue();
        double destLon = destination.longitude().doubleValue();

        return Flux.fromIterable(productIds)
                .flatMap(productId -> inventoryRepository.findInventoryEntitiesByProductId(productId)
                        .flatMap(inventory -> getWarehouseByID(inventory.getWarehouseId())
                                .flatMap(warehouse -> getAddressById(warehouse.getAddressId())
                                        .flatMap(warehouseAddress -> inventoryRepository.distanceBetween(
                                                        warehouseAddress.getLatitude(), warehouseAddress.getLongitude(),
                                                        destLat, destLon)
                                                .map(distance -> new ProductWarehouseCandidate(
                                                        productId,
                                                        warehouse.getWarehouseId(),
                                                        toOrigin(warehouse),
                                                        distance
                                                ))))));
    }

    // ---------------------------------------------------------------------
    // Write port (OrdersGateway)
    // ---------------------------------------------------------------------

    @Override
    public Mono<Order> save(Order order, Long currencyId) {
        return orderRepository.save(orderMapper.toEntity(order, currencyId))
                .flatMap(savedHeader -> {
                    order.setOrderId(savedHeader.getOrder_id());
                    return orderDetailRepository
                            .saveAll(orderMapper.toDetailEntities(savedHeader.getOrder_id(), order))
                            .then(Mono.just(order));
                });
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private Mono<UserEntity> getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User", userId.toString(), "Order")));
    }

    private Mono<AddressEntity> getAddressById(Long addressId) {
        return addressRepository.findById(addressId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Address", addressId.toString(), "Order")));
    }

    private Mono<WarehouseEntity> getWarehouseByID(Long warehouseId) {
        return warehouseRepository.findById(warehouseId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Warehouse", warehouseId.toString(), "Order")));
    }

    private Mono<CityEntity> getCityByID(Long cityId) {
        return cityRepository.findById(cityId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("City", cityId.toString(), "Order")));
    }

    private Mono<CountryEntity> getCountryByID(Long countryId) {
        return countryRepository.findById(countryId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Country", countryId.toString(), "Order")));
    }

    private Mono<DeliveryAddress> buildDeliveryAddressFromUser(UserEntity user){
        return getAddressById(user.getAddressID())
                .flatMap(this::buildDeliveryAddressFromAddress)
    }

    private Mono<DeliveryAddress> buildDeliveryAddressFromAddress(AddressEntity address){
        return getCityByID(address.getCityId())
                .flatMap(cityEntity -> buildDeliveryAddress(address, cityEntity))
    }

    private Mono<DeliveryAddress> buildDeliveryAddress(AddressEntity address, CityEntity city){
        return getCountryByID(city.getCountryId())
                .map(countryEntity -> toDeliveryAddress(address, city, countryEntity));
    }

    private DeliveryAddress toDeliveryAddress(AddressEntity address, CityEntity city, CountryEntity country) {

        // ! The DeliveryAddress is part of the Domain, the use case only sees the domain Entities, ObjectValues and Aggregates
        return new DeliveryAddress(
                address.getAddress(),
                city.getCityName(),
                country.getCountryName(),
                new Coordinates(
                        BigDecimal.valueOf(address.getLatitude()),
                        BigDecimal.valueOf(address.getLongitude())
                )
        );
    }

    private WarehouseOrigin toOrigin(WarehouseEntity warehouse) {
        // TODO: resolve city/country names; only their ids exist on the address today.
        return new WarehouseOrigin(warehouse.getName(), null, null);
    }
}
