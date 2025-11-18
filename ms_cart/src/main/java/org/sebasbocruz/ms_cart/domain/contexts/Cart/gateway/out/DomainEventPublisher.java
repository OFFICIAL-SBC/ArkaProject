package org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartStateEvent;

public interface DomainEventPublisher {
    void publishCartItemEvent(CartItemEvent event);
    void publishCartOrderEvent(CartStateEvent event);
}
