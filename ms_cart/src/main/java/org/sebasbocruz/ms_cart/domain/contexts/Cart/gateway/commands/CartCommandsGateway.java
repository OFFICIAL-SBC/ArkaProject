package org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands;

import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate.Cart;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartId;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.product.ProductEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CartCommandsGateway {
    Cart createNewCart(CartDTO cartDTO);
    Optional<Cart> findByUserIdAndState(Long user_id, CartState cartState);
    Optional<Cart> finCartById(CartId id) ;
    Cart save(Cart cartDomain);
    Map<Long, ProductEntity> fetchProductsById(List<LineDTO> lines);
}
