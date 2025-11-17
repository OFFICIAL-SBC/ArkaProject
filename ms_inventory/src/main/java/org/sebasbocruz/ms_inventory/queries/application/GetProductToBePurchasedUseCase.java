package org.sebasbocruz.ms_inventory.queries.application;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_inventory.queries.domain.gateways.QueryInventoryGateway;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos.StockPolicyDTO;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetProductToBePurchasedUseCase {

    private final QueryInventoryGateway queryInventoryGateway;


    public Mono<StockPolicyDTO> execute(Long productId) {
        return queryInventoryGateway.getAvailableInventoryPolicy(productId);
    }

}
