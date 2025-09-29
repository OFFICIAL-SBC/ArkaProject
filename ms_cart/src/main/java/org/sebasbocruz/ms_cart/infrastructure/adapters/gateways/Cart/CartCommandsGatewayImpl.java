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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

        CartStateEntity cartStateEntity = cartStateRepository.findCartStateEntitiesByCartState(CartState.OPEN);
        UserEntity userEntity = userRepository.findUserEntitiesById(cartDTO.getUserID());
        CurrencyEntity currencyEntity = currencyEntityRepository.findCurrencyEntitiesByCode(cartDTO.getCurrencyCode());

        CartEntity cartEntity =  CartEntity.builder()
                .userEntity(userEntity)
                .cartState(cartStateEntity)
                .currencyEntity(currencyEntity)
                .build();

        List<CartDetailEntity> details = cartDTO.getLines().stream()
                .map(lineDTO -> {
                    ProductEntity productEntity = productEntityRepository.findById(lineDTO.getProductId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "The product with ID " + lineDTO.getProductId() + " was not found"));

                    return CartDetailEntity.builder()
                            .cartEntity(cartEntity)
                            .amount(lineDTO.getNumberOfProducts())
                            .product(productEntity)
                            .build();
                })
                .toList();

        cartEntity.setDetails(details);

        CartEntity cartSaved = cartRepository.save(cartEntity);

        return CartMapper.fromInfrastructureToDomain(cartSaved);
    }

    @Override
    public Optional<Cart> findByUserIdAndState(Long user_id, CartState cartState){
        return cartRepository.findByUserEntity_IdAndCartState_CartState(user_id, cartState).map(CartMapper::fromInfrastructureToDomain);
    }

    @Override
    public Optional<Cart> findById(CartId id) {
        return cartRepository.findById(id.value()).map(CartMapper::fromInfrastructureToDomain);
    }

    @Override
    public Cart save(Cart cartDomain) {
        CartEntity cartEntity = cartRepository.findById(cartDomain.getId().value()).orElseGet(CartEntity::new);

        CartMapper.synchronizeJpaEntityWithDomain(cartDomain, cartEntity, productEntityRepository::getReferenceById);
        return CartMapper.fromInfrastructureToDomain(cartRepository.save(cartEntity));
    }


}
