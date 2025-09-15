package org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands;

import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;

public interface CartCommandsGateway {

    CartDTO createNewCart(CartDTO cartDTO);
}
