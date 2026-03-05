package org.sebasbocruz.ms_orders.infrastructure.entrypoints.Commands;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_orders.application.CreateNewOrderUseCase;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OrderCommandHandler {

    private final CreateNewOrderUseCase createNewOrderUseCase;


    Mono<ServerResponse> createNewOrderFromConvertedCart(ServerRequest serverRequest){

        Long carIDRequested = Long.parseLong(serverRequest.queryParam("cart_id").get());

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createNewOrderUseCase.execute(carIDRequested));
    }

}
