package org.sebasbocruz.ms_cart.infrastructure.adapters.gateways.Cart;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate.Cart;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartId;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands.CartCommandsGateway;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.mapper.CartMapper;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartDetailEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartStateEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.common.CurrencyEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.product.ProductEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.users.UserEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CartCommandsGatewayImpl implements CartCommandsGateway {

    private Logger logger = LoggerFactory.getLogger(CartCommandsGatewayImpl.class);

    // Repositories
    private final CartRepository cartRepository;
    private final CartStateRepository cartStateRepository;
    private final UserRepository userRepository;
    private final CurrencyEntityRepository currencyEntityRepository;
    private final ProductEntityRepository productEntityRepository;



    @Override
    public Cart createNewCart(CartDTO cartDTO) {

        CartStateEntity cartStateEntity = cartStateRepository.findCartStateEntitiesByCartState(CartState.OPEN)
                 .orElseThrow(() -> new EntityNotFoundException("Cart state not found"));

        UserEntity userEntity = userRepository.findUserEntitiesById(cartDTO.getUserID())
                .orElseThrow(() -> new EntityNotFoundException("User " + cartDTO.getUserID() + " not found"));

        CurrencyEntity currencyEntity = currencyEntityRepository.findCurrencyEntitiesByCode(cartDTO.getCurrencyCode())
                .orElseThrow(() -> new EntityNotFoundException("Currency " + cartDTO.getCurrencyCode() + " not found"));

        Map<Long, ProductEntity> productsById = fetchProductsById(cartDTO.getLines());

        CartEntity cartEntity = buildCartEntity(userEntity, currencyEntity, cartStateEntity, cartDTO, productsById);

        CartEntity cartSaved = cartRepository.save(cartEntity);

        return CartMapper.fromInfrastructureToDomain(cartSaved);
    }


    @Override
    public Optional<Cart> findCartByUserIdAndState(Long user_id, CartState cartState){
        return cartRepository.findByUserEntity_IdAndCartState_CartState(user_id, cartState).map(CartMapper::fromInfrastructureToDomain);
    }

    @Override
    public Optional<Cart> findCartById(CartId id) {
        return cartRepository.findById(id.value()).map(CartMapper::fromInfrastructureToDomain);
    }

    @Override
    public Cart save(Cart cartDomain) {
        CartEntity cartEntity = cartRepository.findById(cartDomain.getId().value())
                .orElseThrow(() -> new EntityNotFoundException("The CART with id "+ cartDomain.getId().value()+"does not EXIST"));

        CartMapper.synchronizeJpaEntityWithDomain(cartDomain, cartEntity, productEntityRepository::getReferenceById);
        CartStateEntity cartStateEntity = cartStateRepository.findCartStateEntitiesByCartState(cartDomain.getState())
                .orElseThrow(() -> new EntityNotFoundException("Cart state not found"));

        cartEntity.setCartState(cartStateEntity);

        return CartMapper.fromInfrastructureToDomain(cartRepository.save(cartEntity));
    }


    @Override
    public Map<Long, ProductEntity> fetchProductsById(List<LineDTO> lines) {
        Set<Long> ids = lines.stream().map(LineDTO::getProductId).collect(Collectors.toSet());
        List<ProductEntity> found = productEntityRepository.findAllById(ids);
        Map<Long, ProductEntity> map = found.stream().collect(Collectors.toMap(ProductEntity::getId, p -> p));

        // ensure all exist
        for (Long id : ids) {
            if (!map.containsKey(id)) {
                throw new EntityNotFoundException("The product with ID " + id + " was not found");
            }
        }
        return map;
    }

    private CartEntity buildCartEntity(
            UserEntity user,
            CurrencyEntity currency,
            CartStateEntity openState,
            CartDTO cartDTO,
            Map<Long, ProductEntity> productsById
    ) {
        CartEntity cart = CartEntity.builder()
                .userEntity(user)
                .cartState(openState)
                .currencyEntity(currency)
                .build();

        List<CartDetailEntity> details = cartDTO.getLines().stream()
                .map(line -> CartDetailEntity.builder()
                        .cartEntity(cart)
                        .amount(line.getNumberOfProducts())
                        .product(productsById.get(line.getProductId()))
                        .build())
                .toList();

        cart.setDetails(details);
        return cart;
    }

}
