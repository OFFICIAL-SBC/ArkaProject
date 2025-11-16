package org.sebasbocruz.ms_inventory.commands.application;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.gateway.CommandsInventoryGateway;

@RequiredArgsConstructor
public class CartQuantityItemChangedUseCase {

    private final CommandsInventoryGateway inventoryGatewayCommands;


    public void execute(Long cartId, Long productId, int quantity) {
        inventoryGatewayCommands.handleCartQuantityItemChanged(cartId, productId, quantity);
    }

}
