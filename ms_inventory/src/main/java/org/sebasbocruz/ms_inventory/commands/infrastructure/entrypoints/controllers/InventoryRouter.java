package org.sebasbocruz.ms_inventory.commands.infrastructure.entrypoints.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class InventoryRouter {

    @Bean
    public RouterFunction<ServerResponse> inventoryRoutes(InventoryHandler inventoryHandler){
        return RouterFunctions.route().
                GET("/api/v1/inventory", inventoryHandler::getInventory)
                .GET("/api/v1/inventory/stream", inventoryHandler::streamEvents)
                .build();
    }

}
