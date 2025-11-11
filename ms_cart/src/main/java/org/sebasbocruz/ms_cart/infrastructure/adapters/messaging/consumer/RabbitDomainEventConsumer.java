package org.sebasbocruz.ms_cart.infrastructure.adapters.messaging.consumer;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children.CartOpened;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.in.DomainEventConsumer;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

@Service
@RequiredArgsConstructor
public class RabbitDomainEventConsumer implements DomainEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitDomainEventConsumer.class);

    @Override
    @RabbitListener(
            queues = "cart.notifications.queue"
    )
    public Object consume(CartItemEvent cartItemEvent) {

        LOGGER.warn("Message received from Queue Consumer {}", cartItemEvent.toString());

        return null;
    }
}
