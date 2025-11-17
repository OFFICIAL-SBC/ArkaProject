package org.sebasbocruz.ms_inventory.queries.domain.gateways;


import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos.InventoryDTOquery;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos.StockPolicyDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QueryInventoryGateway {

    Flux<InventoryDTOquery> getProductsToBeSupplied();
    Mono<StockPolicyDTO> getAvailableInventoryPolicy(Long productId);

}
