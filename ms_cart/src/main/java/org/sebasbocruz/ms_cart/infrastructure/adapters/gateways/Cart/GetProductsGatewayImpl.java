package org.sebasbocruz.ms_cart.infrastructure.adapters.gateways.Cart;

import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.query.GetProductsGateway;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.mapper.CartMapper;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetProductsGatewayImpl implements GetProductsGateway {

    private final CartRepository cartRepository;


    private GetProductsGatewayImpl(CartRepository cartRepository,CartMapper cartMapper){
        this.cartRepository = cartRepository;
    }

    @Override
    public Optional<CartDTO> getOpenedCartByUserID(Long userId) {
        Optional<CartEntity> result = cartRepository.findByUserEntity_IdAndCartState_CartState(userId, CartState.OPEN);

        return result.map(CartMapper::fromInfrastructureToClient);

    }
}
