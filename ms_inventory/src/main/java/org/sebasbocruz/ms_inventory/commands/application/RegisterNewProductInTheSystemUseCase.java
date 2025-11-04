package org.sebasbocruz.ms_inventory.commands.application;

import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Entity.Warehouse;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.gateway.CommandsInventoryGateway;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.dtos.InventoryDTOCommandsResponse;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.dtos.InventoryDTOcommands;
import reactor.core.publisher.Mono;

public class RegisterNewProductInTheSystemUseCase {

    private final CommandsInventoryGateway inventoryGatewayCommands;

    public RegisterNewProductInTheSystemUseCase(CommandsInventoryGateway inventoryGateway) {
        this.inventoryGatewayCommands = inventoryGateway;
    }

    public Mono<InventoryDTOCommandsResponse> registerNewProduct(InventoryDTOcommands inventoryDTOcommands) {

        Mono<Warehouse> warehouseMono =
                inventoryGatewayCommands.doesWarehouseExist(inventoryDTOcommands.getWarehouseId())
                        .switchIfEmpty(Mono.error(new IllegalArgumentException(
                                "Warehouse with ID " + inventoryDTOcommands.getWarehouseId() + " does not exist."
                        )));


        Mono<Void> productNotExistsByNameCheck =
                inventoryGatewayCommands.doesProductExistByName(inventoryDTOcommands.getName())
                        .flatMap(exists -> exists
                                        ? Mono.error(new IllegalArgumentException(
                                        "Product " + inventoryDTOcommands.getName() + " already exists."
                                ))
                                        : Mono.empty()
                        );



        Mono<Void> brandExistsCheck =
                inventoryGatewayCommands.doesBrandExist(inventoryDTOcommands.getBrandId())
                        .flatMap(exists -> exists
                                        ? Mono.empty()
                                        : Mono.error(new IllegalArgumentException(
                                        "Brand with ID " + inventoryDTOcommands.getBrandId() + " does not exist."
                                ))
                        );


        Mono<Void> categoryExistsCheck =
                inventoryGatewayCommands.doesCategoryExist(inventoryDTOcommands.getCategoryId())
                        .flatMap(exists -> exists
                                        ? Mono.empty()
                                        : Mono.error(new IllegalArgumentException(
                                        "Category with ID " + inventoryDTOcommands.getCategoryId() + " does not exist."
                                ))
                        );


        return warehouseMono.flatMap(warehouse ->
                Mono.when(productNotExistsByNameCheck, brandExistsCheck, categoryExistsCheck)
                        .then(inventoryGatewayCommands.registerNewProduct(inventoryDTOcommands))
                        .map(inventory -> InventoryDTOCommandsResponse.builder()
                                .productId(inventory.getId())
                                .productName(inventoryDTOcommands.getName())
                                .countryName(warehouse.getCountry())
                                .cityName(warehouse.getCity())
                                .warehouseName(warehouse.getName())
                                .warehouseAddress(warehouse.getAddress())
                                .warehouseId(inventory.getWarehouseId())
                                .initialStock(inventory.getAvailable().value())
                                .thresholdStock(inventory.getThreshold().value())
                                .build()
                        )
        );
    }

}
