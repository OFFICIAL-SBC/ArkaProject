package org.sebasbocruz.ms_inventory.commands.application;

import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate.Inventory;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.gateway.InventoryGateway;
import reactor.core.publisher.Flux;


public class GetProductsToBeSuppliedUseCase{

    private final InventoryGateway inventoryGateway;

    public GetProductsToBeSuppliedUseCase(InventoryGateway inventoryGateway) {
        this.inventoryGateway = inventoryGateway;
    }

    public Flux<Inventory> getProductsToBeSupplied() {
        return inventoryGateway.getProductsToBeSupplied();
    }

}
