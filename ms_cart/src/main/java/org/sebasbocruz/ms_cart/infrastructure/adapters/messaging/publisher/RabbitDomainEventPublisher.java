package org.sebasbocruz.ms_cart.infrastructure.adapters.messaging.publisher;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.DomainEventPublisher;
import org.sebasbocruz.ms_cart.infrastructure.adapters.messaging.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitDomainEventPublisher implements DomainEventPublisher {
    private final RabbitTemplate amqp;

    @Override
    public void publish(Object event) {
//        String rk = "cart." + event.getClass().getSimpleName().replaceAll("([A-Z])", "_$1")
//                .toLowerCase().substring(1); // cart.cart_opened

        String rk = "cart.event.routing.key";
        amqp.convertAndSend(RabbitConfig.EXCHANGE, rk, event);
    }
}