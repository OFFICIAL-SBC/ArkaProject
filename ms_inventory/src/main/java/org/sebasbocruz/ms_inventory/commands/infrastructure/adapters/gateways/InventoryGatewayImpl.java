package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.gateways;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate.Inventory;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.gateway.InventoryGateway;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.Mappers.InventoryMapper;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.InventoryEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.InventoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class InventoryGatewayImpl implements InventoryGateway {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public Flux<Inventory> getProductsToBeSupplied() {
        inventoryRepository.getProductsThatStockIsBelowThreshold()
                .subscribe(
                        inventoryEntity -> {
                            // Here you can process each inventoryEntity as it is emitted
                            System.out.println("Product to be supplied: " + inventoryEntity.toString());
                        },
                        error -> {
                            // Handle any errors that occur during the stream processing
                            System.err.println("Error occurred: " + error.getMessage());
                        },
                        () -> {
                            // This will be called when the stream is complete
                            System.out.println("Completed fetching products to be supplied.");
                        }
                );

        return null;
    }

}
