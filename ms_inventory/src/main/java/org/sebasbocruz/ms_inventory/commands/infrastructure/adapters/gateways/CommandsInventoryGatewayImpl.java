package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.gateways;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_inventory.commands.domain.commons.movement.MovementType;
import org.sebasbocruz.ms_inventory.commands.domain.commons.reference.ReferenceType;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate.Inventory;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Entity.Warehouse;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.ValueObjects.Quantity;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.gateway.CommandsInventoryGateway;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.Mappers.CommandsProductMapper;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.dtos.InventoryDTOcommands;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.InventoryEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.MovementEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.product.ProductEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.publics.CityEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.publics.CountryEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.inventory.MovementRepositoryCommands;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.publics.AddressRepositoryCommands;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.inventory.InventoryRepositoryCommands;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.product.BrandRepositoryCommands;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.product.CategoryRepositoryCommands;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.product.ProductRepositoryCommands;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.inventory.WarehouseRepositoryCommands;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.publics.CityRepositoryCommands;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.repositories.schema.publics.CountryRepositoryCommands;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.rmi.NoSuchObjectException;
import java.time.Instant;

@RequiredArgsConstructor
@Service
public class CommandsInventoryGatewayImpl implements CommandsInventoryGateway {

    private final InventoryRepositoryCommands inventoryRepository;
    private final ProductRepositoryCommands productRepository;
    private final WarehouseRepositoryCommands warehouseRepository;
    private final CategoryRepositoryCommands categoryRepository;
    private final BrandRepositoryCommands brandRepository;
    private final AddressRepositoryCommands addressRepository;
    private final CityRepositoryCommands cityRepository;
    private final CountryRepositoryCommands countryRepository;
    private final MovementRepositoryCommands movementRepository;

    private final CommandsProductMapper commandsProductMapper;




    @Override
    public Mono<Inventory> registerNewProduct(InventoryDTOcommands inventoryDTOcommands) {

        Mono<ProductEntity> productEntitySaved = productRepository.save(
                commandsProductMapper.fromClientToEntity(
                        inventoryDTOcommands.getBrandId(),
                        inventoryDTOcommands.getCategoryId(),
                        inventoryDTOcommands.getName(),
                        inventoryDTOcommands.getDescription(),
                        inventoryDTOcommands.getPrice(),
                        inventoryDTOcommands.getDiscount()
                )
        );

        Mono<InventoryEntity> inventoryEntitySaved = productEntitySaved.flatMap(productEntity ->
                inventoryRepository.save(
                        InventoryEntity.builder()
                                .productId(productEntity.getProductId())
                                .warehouseId(inventoryDTOcommands.getWarehouseId())
                                .availableStock(inventoryDTOcommands.getInitialStock())
                                .thresholdStock(inventoryDTOcommands.getThresholdStock())
                                .build()
                )
        );

        return inventoryEntitySaved.flatMap(inventoryEntity ->
                Mono.just(
                        new Inventory(
                                inventoryEntity.getInventoryId(),
                                inventoryEntity.getProductId(),
                                inventoryEntity.getWarehouseId(),
                                new Quantity(inventoryEntity.getAvailableStock()),
                                new Quantity(inventoryEntity.getThresholdStock())
                        )
                )
        );
    }


    public Mono<Warehouse> doesWarehouseExist(Long warehouseId) {
        return warehouseRepository.findById(warehouseId).
                flatMap(
                        warehouseEntity -> {
                            return addressRepository.findById(warehouseEntity.getAddressId())
                                    .flatMap(addressEntity -> Mono.zip(
                                                    countryRepository.findById(addressEntity.getCountryId()),
                                                    cityRepository.findById(addressEntity.getCityId())
                                    ).map(tuple -> {
                                        CountryEntity countryEntity = tuple.getT1();
                                        CityEntity cityEntity = tuple.getT2();

                                        return new Warehouse(
                                                warehouseEntity.getWarehouseId(),
                                                warehouseEntity.getName(),
                                                addressEntity.getAddress(),
                                                countryEntity.getName(),
                                                cityEntity.getName()

                                        );
                                    }));
                        }
                );
    }

    @Override
    public Mono<Void> handleCartItemAddedToCart(Long cartId, Long productId, int quantity) {
        return inventoryRepository.findInventoryEntityByProductId(productId)
                .switchIfEmpty(Mono.error(new NoSuchObjectException("Product with ID " + productId + " not found in any Warehouse.")))
                .flatMap(inventoryEntity -> {
                    int updatedStock = inventoryEntity.getAvailableStock() - quantity;
                    inventoryEntity.setAvailableStock(updatedStock);
                    return inventoryRepository.save(inventoryEntity).flatMap(savedEntity -> {
                        // Optionally, log the movement
                        return movementRepository.save(
                                MovementEntity.builder()
                                        .inventoryId(savedEntity.getInventoryId())
                                        .movementType(MovementType.SUBTRACT.name())
                                        .referenceType(ReferenceType.CART.name())
                                        .quantity(quantity)
                                        .build()
                        );
                    });
                })
                .then();
    }

    @Override
    public Mono<Void> handleRemoveItemFromCart(Long cartId, Long productId, int quantity) {
        return inventoryRepository.findInventoryEntityByProductId(productId)
                .switchIfEmpty(Mono.error(new NoSuchObjectException("Product with ID " + productId + " not found in any Warehouse.")))
                .flatMap(inventoryEntity -> {
                    int updatedStock = inventoryEntity.getAvailableStock() + quantity;
                    inventoryEntity.setAvailableStock(updatedStock);
                    return inventoryRepository.save(inventoryEntity)
                            .flatMap(savedEntity -> {
                                // Optionally, log the movement
                                return movementRepository.save(
                                        MovementEntity.builder()
                                                .inventoryId(savedEntity.getInventoryId())
                                                .movementType(MovementType.ADD.name())
                                                .referenceType(ReferenceType.CART.name())
                                                .quantity(quantity)
                                                .build()
                                );
                            });
                })
                .then();
    }

    @Override
    public Mono<Void> handleCartQuantityItemChanged(Long cartId, Long productId, int quantityDifference) {
        return inventoryRepository.findInventoryEntityByProductId(productId)
                .switchIfEmpty(Mono.error(new NoSuchObjectException("Product with ID " + productId + " not found in any Warehouse.")))
                .flatMap(inventoryEntity -> {
                    int updatedStock = inventoryEntity.getAvailableStock() - quantityDifference;
                    inventoryEntity.setAvailableStock(updatedStock);
                    return inventoryRepository.save(inventoryEntity)
                            .flatMap(savedEntity -> {
                                // Optionally, log the movement
                                return movementRepository.save(
                                        MovementEntity.builder()
                                                .inventoryId(savedEntity.getInventoryId())
                                                .movementType(quantityDifference < 0 ? MovementType.ADD.name(): MovementType.SUBTRACT.name())
                                                .referenceType(ReferenceType.CART.name())
                                                .quantity(quantityDifference < 0 ? -quantityDifference : quantityDifference)
                                                .build()
                                );
                            });
                })
                .then();
    }

    @Override
    public Mono<Boolean> doesProductExistByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Mono<Boolean> doesProductExistById(Long product_id) {
        return productRepository.existsById(product_id);
    }

    @Override
    public Mono<Boolean> doesBrandExist(Long brandId) {
        return brandRepository.existsById(brandId);
    }

    @Override
    public Mono<Boolean> doesCategoryExist(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }
}
