package org.sebasbocruz.ms_inventory.commands.infrastructure.entrypoints.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class CommandsInventoryRouter {

    @Bean
    public RouterFunction<ServerResponse> inventoryRoutes(CommandsInventoryHandler inventoryHandler){
        return RouterFunctions.route().
                POST("/api/v1/inventory", inventoryHandler::getInventory)
                .POST("/api/v1/inventory/stream", inventoryHandler::streamEvents)
                .build();
    }

}
