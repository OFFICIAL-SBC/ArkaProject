package org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;

public interface DomainEventPublisher {
    void publish(CartItemEvent event);
}
