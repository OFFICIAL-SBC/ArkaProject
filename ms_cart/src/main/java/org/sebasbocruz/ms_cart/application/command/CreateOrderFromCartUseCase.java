package org.sebasbocruz.ms_cart.application.command;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands.CartCommandsGateway;

@RequiredArgsConstructor
public class CreateOrderFromCartUseCase {
    private final CartCommandsGateway cartCommandsGateway;
}
