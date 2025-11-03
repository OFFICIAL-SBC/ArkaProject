package org.sebasbocruz.ms_inventory.queries.application;

import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate.Inventory;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.gateway.CommandsInventoryGateway;
import org.sebasbocruz.ms_inventory.queries.domain.gateways.QueryInventoryGateway;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos.InventoryDTO;
import reactor.core.publisher.Flux;


public class GetProductsToBeSuppliedUseCase{

    private final QueryInventoryGateway inventoryGateway;

    public GetProductsToBeSuppliedUseCase(QueryInventoryGateway inventoryGateway) {
        this.inventoryGateway = inventoryGateway;
    }

    public Flux<InventoryDTO> getProductsToBeSupplied() {
        return inventoryGateway.getProductsToBeSupplied();
    }

}
