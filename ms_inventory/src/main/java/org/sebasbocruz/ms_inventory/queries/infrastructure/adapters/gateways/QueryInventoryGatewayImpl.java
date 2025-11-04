package org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.gateways;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.publics.AddressEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.WarehouseEntity;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.product.ProductEntity;
import org.sebasbocruz.ms_inventory.queries.domain.gateways.QueryInventoryGateway;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos.InventoryDTOquery;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.mappers.QueryInventoryMapper;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.repositories.QueryAddressRepository;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.repositories.QueryInventoryRepository;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.repositories.QueryProductRepository;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.repositories.QueryWarehouseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class QueryInventoryGatewayImpl implements QueryInventoryGateway {

    private final QueryInventoryRepository inventoryRepository;
    private final QueryProductRepository productRepository;
    private final QueryInventoryMapper inventoryMapper;
    private final QueryWarehouseRepository warehouseRepository;
    private final QueryAddressRepository addressRepository;

    public QueryInventoryGatewayImpl(QueryInventoryRepository inventoryRepository, QueryProductRepository productRepository, QueryInventoryMapper inventoryMapper, QueryWarehouseRepository warehouseRepository, QueryAddressRepository addressRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.inventoryMapper = inventoryMapper;
        this.warehouseRepository = warehouseRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Flux<InventoryDTOquery> getProductsToBeSupplied() {
        return inventoryRepository.getProductsThatStockIsBelowThreshold() // Flux<InventoryEntity>
                .flatMap(inventoryEntity ->
                                Mono.zip(
                                                fetchProduct(inventoryEntity.getProductId()),      // Mono<ProductEntity> o error
                                                fetchWarehouse(inventoryEntity.getWarehouseId())    // Mono<WarehouseEntity> o error
                                        )
                                        .flatMap(tuple -> {
                                            ProductEntity productEntity = tuple.getT1();
                                            WarehouseEntity warehouseEntity = tuple.getT2();
                                            return fetchAddress(warehouseEntity.getAddressId())       // Mono<AddressEntity> o error
                                                    .map(addressEntity ->
                                                            inventoryMapper.fromEntityToDTO(
                                                                    inventoryEntity, productEntity, warehouseEntity, addressEntity
                                                            )
                                                    );
                                        })
                );
    }


    private Mono<ProductEntity>fetchProduct (Long productId) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found. id=" + productId
                )));
    }

    private Mono<WarehouseEntity> fetchWarehouse(Long warehouseId) {
        return warehouseRepository.findById(warehouseId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Warehouse not found. id=" + warehouseId
                )));
    }

    private Mono<AddressEntity> fetchAddress(Long addressId) {
        return addressRepository.findById(addressId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Address not found. id=" + addressId
                )));
    }
}
