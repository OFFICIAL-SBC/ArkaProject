package org.sebasbocruz.ms_cart.infrastructure.adapters.messaging.publisher;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.DomainEventPublisher;
import org.sebasbocruz.ms_cart.infrastructure.adapters.messaging.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitDomainEventPublisher implements DomainEventPublisher {

    private final Logger logger = LoggerFactory.getLogger(RabbitDomainEventPublisher.class);

    private final RabbitTemplate amqp;

    @Override
    public void publish(Object event) {
        String rk = event.getClass().getSimpleName().replaceAll("([A-Z])", ".$1")
                .toLowerCase().substring(1); // cart.cart_opened

        logger.warn("This is the Routing Key of the event {}", rk);

        amqp.convertAndSend(RabbitConfig.EXCHANGE, rk, event);
    }
}