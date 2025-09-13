package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.gateways;

import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.query.GetProductsGateway;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.repositories.CartRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GetProductsGatewayImpl implements GetProductsGateway {

    private final CartRepository cartRepository;

    private GetProductsGatewayImpl(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    @Override
    public Optional<CartEntity> getCartByUserID(Long userId) {
        return  cartRepository.findByUserEntity_IdAndCartState_CartState(userId, CartState.OPEN);
    }
}
