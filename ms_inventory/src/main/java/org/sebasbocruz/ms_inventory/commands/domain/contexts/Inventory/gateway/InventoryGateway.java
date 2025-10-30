package org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.gateway;

import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate.Inventory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InventoryGateway {

    Flux<Inventory> getProductsToBeSupplied();

}
