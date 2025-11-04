package org.sebasbocruz.ms_inventory.queries.domain.gateways;


import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos.InventoryDTOquery;
import reactor.core.publisher.Flux;

public interface QueryInventoryGateway {

    Flux<InventoryDTOquery> getProductsToBeSupplied();

}
