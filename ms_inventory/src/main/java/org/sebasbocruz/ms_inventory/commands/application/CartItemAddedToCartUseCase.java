package org.sebasbocruz.ms_inventory.commands.application;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.gateway.CommandsInventoryGateway;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CartItemAddedToCartUseCase {

    private final CommandsInventoryGateway inventoryGatewayCommands;

    public Mono<Void> execute(Long cartId, Long productId, int quantity) {
        return inventoryGatewayCommands.handleCartItemAddedToCart(cartId, productId, quantity);
    }

}
