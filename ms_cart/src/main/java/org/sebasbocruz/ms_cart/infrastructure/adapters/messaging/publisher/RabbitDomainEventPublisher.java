package org.sebasbocruz.ms_cart.infrastructure.adapters.messaging.publisher;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.DomainEventPublisher;
import org.sebasbocruz.ms_cart.infrastructure.adapters.messaging.config.RabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitDomainEventPublisher implements DomainEventPublisher {
    private final AmqpTemplate amqp;

    @Override
    public void publish(Object event) {
        String rk = "cart." + event.getClass().getSimpleName().replaceAll("([A-Z])", "_$1")
                .toLowerCase().substring(1); // cart.cart_opened
        amqp.convertAndSend(RabbitConfig.EXCHANGE, rk, event);
    }
}