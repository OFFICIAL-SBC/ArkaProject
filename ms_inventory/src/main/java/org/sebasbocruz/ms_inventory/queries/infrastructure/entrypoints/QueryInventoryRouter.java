package org.sebasbocruz.ms_inventory.queries.infrastructure.entrypoints;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class QueryInventoryRouter {

    @Bean
    public RouterFunction<ServerResponse> routeInventoryQueries(QueryInventoryHandler queryInventoryHandler) {
        return RouterFunctions
                .route()
                .GET("/api/v1/inventory/products-to-be-supplied", queryInventoryHandler::getProductsToBeSupplied)
                .GET("/api/v1/inventory/report/pdf", queryInventoryHandler::generatePdfReport)
                .GET("/api/v1/inventory/available", queryInventoryHandler::getAvailableInventoryPolicy)
                .build();
    }
}
