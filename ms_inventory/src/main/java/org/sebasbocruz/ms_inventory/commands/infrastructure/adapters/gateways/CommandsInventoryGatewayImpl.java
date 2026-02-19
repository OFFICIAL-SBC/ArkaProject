package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.gateways;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_inventory.commands.domain.commons.errors.DataAccessException;
import org.sebasbocruz.ms_inventory.commands.domain.commons.errors.EntityNotFoundException;
import org.sebasbocruz.ms_inventory.commands.domain.commons.movement.MovementType;
import org.sebasbocruz.ms_inventory.commands.domain.commons.reference.ReferenceType;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate.Inventory;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Entity.Warehouse;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.ValueObjects.Quantity;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.gateway.CommandsInventoryGateway;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.Mappers.CommandsInventoryMapper;
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
    private final CommandsInventoryMapper commandsInventoryMapper;



    @Override
    public Mono<Inventory> registerNewProduct(InventoryDTOcommands inventoryDTOcommands) {

        return productRepository.save(
                commandsProductMapper.fromClientToEntity(
                        inventoryDTOcommands.getBrandId(),
                        inventoryDTOcommands.getCategoryId(),
                        inventoryDTOcommands.getName(),
                        inventoryDTOcommands.getDescription(),
                        inventoryDTOcommands.getPrice(),
                        inventoryDTOcommands.getDiscount()
                )
        ).switchIfEmpty(Mono.error(new DataAccessException("Product "+inventoryDTOcommands.getName()+" could not be saved")))
                .flatMap(productEntity -> {
                    return inventoryRepository.save(
                            InventoryEntity.builder()
                                    .productId(productEntity.getProductId())
                                    .warehouseId(inventoryDTOcommands.getWarehouseId())
                                    .availableStock(inventoryDTOcommands.getInitialStock())
                                    .thresholdStock(inventoryDTOcommands.getThresholdStock())
                                    .build()
                    ).switchIfEmpty(Mono.error(new DataAccessException("Inventory for product"+inventoryDTOcommands.getName()+" could not be saved")));
                }).flatMap(
                        inventoryEntity -> {
                            return Mono.just(
                                    commandsInventoryMapper.fromEntityToDomain(inventoryEntity)
                            );
                        }
                ).onErrorMap(error -> {
                    if(!(error instanceof DataAccessException)){
                        return new RuntimeException("Unexpected error when trying to add the product "+inventoryDTOcommands.getName());
                    }
                    return error;
                });
    }


    public Mono<Warehouse> doesWarehouseExist(Long warehouseId) {
        return warehouseRepository.findById(warehouseId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Warehouse with id "+warehouseId+" was not found")))
                .flatMap(
                        warehouseEntity -> {
                            return addressRepository.findById(warehouseEntity.getAddressId())
                                    .switchIfEmpty(Mono.error(new EntityNotFoundException("Address with ID"+warehouseEntity.getAddressId()+" was not found")))
                                    .flatMap(addressEntity ->
                                        Mono.zip(
                                                countryRepository.findById(addressEntity.getCountryId())
                                                        .switchIfEmpty(Mono.error(new EntityNotFoundException("Country with ID"+addressEntity.getCountryId()+" was not found"))),
                                                cityRepository.findById(addressEntity.getCityId())
                                                        .switchIfEmpty(Mono.error(new EntityNotFoundException("Country with ID"+addressEntity.getCityId()+" was not found")))
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
                ).onErrorMap(
                        originalError -> {
                            if (!(originalError instanceof EntityNotFoundException )){
                                return new RuntimeException("Unexpected error when fetching warehouse with id "+warehouseId,originalError);
                            }

                            return originalError; // ! pass EntityNotFoundException through unchanged
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
