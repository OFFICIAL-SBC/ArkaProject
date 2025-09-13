package org.sebasbocruz.ms_cart.infrastructure.adapters.messaging.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "cart.events";
    // sugerencia de routing keys: cart.opened, cart.item.added, cart.converted...

    @Bean
    public TopicExchange cartExchange() { return new TopicExchange(EXCHANGE, true, false); }
}