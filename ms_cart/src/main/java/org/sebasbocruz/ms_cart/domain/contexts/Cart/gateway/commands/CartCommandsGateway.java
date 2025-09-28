package org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands;

import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;

import java.util.Optional;

public interface CartCommandsGateway {

    CartDTO createNewCart(CartDTO cartDTO);
    LineDTO addItemToAnExistingCart(Long cart_id, LineDTO newLine);
}
