package org.sebasbocruz.ms_orders.infrastructure.entrypoints.Commands;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class OrderCommandRouter {

    @Bean
    public RouterFunction<ServerResponse> orderRoutes(OrderCommandHandler handler){
        return RouterFunctions.route()
                .POST("/api/v1/order/create", handler::createNewOrderFromConvertedCart)
                .build();
    }

}
