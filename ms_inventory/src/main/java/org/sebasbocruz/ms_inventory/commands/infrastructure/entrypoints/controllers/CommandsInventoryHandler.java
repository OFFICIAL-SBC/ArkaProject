package org.sebasbocruz.ms_inventory.commands.infrastructure.entrypoints.controllers;

import lombok.RequiredArgsConstructor;


import org.sebasbocruz.ms_inventory.commands.application.RegisterNewProductInTheSystemUseCase;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.dtos.InventoryDTOcommands;
import org.springframework.http.HttpStatus;
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

    private final RegisterNewProductInTheSystemUseCase registerNewProductInTheSystemUseCase;

    Mono<ServerResponse> registerNewProduct(ServerRequest request){
        return request.bodyToMono(InventoryDTOcommands.class)
                .flatMap(registerNewProductInTheSystemUseCase::registerNewProduct)
                .flatMap(inventoryDTOCommandsResponse ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(inventoryDTOCommandsResponse)
                );
    }


}
