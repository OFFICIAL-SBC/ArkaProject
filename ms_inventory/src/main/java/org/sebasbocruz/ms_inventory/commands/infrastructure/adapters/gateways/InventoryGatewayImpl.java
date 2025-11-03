package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.gateways;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate.Inventory;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.gateway.CommandsInventoryGateway;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.InventoryRepositoryCommands;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
public class InventoryGatewayImpl implements CommandsInventoryGateway {

    private final InventoryRepositoryCommands inventoryRepository;


    @Override
    public Flux<Inventory> getProductsToBeSupplied() {

        return null;
    }

}
