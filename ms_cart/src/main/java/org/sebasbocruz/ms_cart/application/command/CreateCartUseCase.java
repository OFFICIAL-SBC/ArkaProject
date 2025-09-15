package org.sebasbocruz.ms_cart.application.command;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands.CartCommandsGateway;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;

@RequiredArgsConstructor
public class CreateCartUseCase {

    private final CartCommandsGateway cartCommandsGateway;

    public CartDTO createCart(CartDTO carDTO){
        return cartCommandsGateway.createNewCart(carDTO);
    }

}
