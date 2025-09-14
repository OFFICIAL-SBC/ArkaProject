package org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.query;

import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;

import java.util.Optional;

public interface GetProductsGateway {

    Optional<CartDTO> getOpenedCartByUserID(Long userId);
}
