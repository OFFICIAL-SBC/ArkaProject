package org.sebasbocruz.ms_inventory.queries.application;


import org.sebasbocruz.ms_inventory.queries.domain.gateways.QueryInventoryGateway;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos.InventoryDTOquery;
import reactor.core.publisher.Flux;


public class GetProductsToBeSuppliedUseCase{

    private final QueryInventoryGateway inventoryGateway;

    public GetProductsToBeSuppliedUseCase(QueryInventoryGateway inventoryGateway) {
        this.inventoryGateway = inventoryGateway;
    }

    public Flux<InventoryDTOquery> getProductsToBeSupplied() {
        return inventoryGateway.getProductsToBeSupplied();
    }

}
