package org.sebasbocruz.ms_inventory.queries.domain.gateways;

import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate.Inventory;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos.InventoryDTO;
import reactor.core.publisher.Flux;

public interface QueryInventoryGateway {

    Flux<InventoryDTO> getProductsToBeSupplied();

}
