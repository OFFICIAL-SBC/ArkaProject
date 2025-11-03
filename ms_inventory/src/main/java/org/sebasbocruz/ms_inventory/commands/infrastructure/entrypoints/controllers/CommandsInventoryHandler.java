package org.sebasbocruz.ms_inventory.commands.infrastructure.entrypoints.controllers;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_inventory.queries.application.GetProductsToBeSuppliedUseCase;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate.Inventory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommandsInventoryHandler {

    private final GetProductsToBeSuppliedUseCase getProductsToBeSuppliedUseCase;

    public Mono<ServerResponse> getInventory(ServerRequest request) {
        List<String> inventoryItems = List.of("item1", "item2", "item3");
        Flux<String> inventoryFlux = Flux.fromIterable(inventoryItems);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(inventoryFlux, String.class);
    }

    public Mono<ServerResponse> streamEvents(ServerRequest req) {
        Flux<ServerSentEvent<String>> events =
                Flux.interval(Duration.ofSeconds(1))
                        .map(seq -> ServerSentEvent.builder("Evento #" + seq).build());

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(events, ServerSentEvent.class);
    }

    public Mono<ServerResponse> getProductsToBeSupplied(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(getProductsToBeSuppliedUseCase.getProductsToBeSupplied(), Inventory.class);
    }


}
