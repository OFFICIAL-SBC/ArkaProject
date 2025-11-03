package org.sebasbocruz.ms_inventory.queries.infrastructure.entrypoints;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_inventory.queries.application.GetProductsToBeSuppliedUseCase;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos.InventoryDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class QueryInventoryHandler {

    private final GetProductsToBeSuppliedUseCase getProductsToBeSuppliedUseCase;


    Mono<ServerResponse> getProductsToBeSupplied(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(getProductsToBeSuppliedUseCase.getProductsToBeSupplied(), InventoryDTO.class);

    }
}
