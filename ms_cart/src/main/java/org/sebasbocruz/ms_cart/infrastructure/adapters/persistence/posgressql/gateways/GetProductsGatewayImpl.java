package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.gateways;

import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.query.GetProductsGateway;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.mapper.CartMapper;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.repositories.CartRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class GetProductsGatewayImpl implements GetProductsGateway {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    private GetProductsGatewayImpl(CartRepository cartRepository,CartMapper cartMapper){
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public Optional<CartDTO> getOpenedCartByUserID(Long userId) {
        Optional<CartEntity> result = cartRepository.findByUserEntity_IdAndCartState_CartState(userId, CartState.OPEN);

        if(result.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(cartMapper.fromInfrastructureToClient(result.get()));

    }
}
