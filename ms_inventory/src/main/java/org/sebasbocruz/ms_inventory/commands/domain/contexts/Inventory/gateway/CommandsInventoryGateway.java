package org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.gateway;

import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate.Inventory;
import reactor.core.publisher.Flux;

public interface CommandsInventoryGateway {

    Flux<Inventory> getProductsToBeSupplied();

}
