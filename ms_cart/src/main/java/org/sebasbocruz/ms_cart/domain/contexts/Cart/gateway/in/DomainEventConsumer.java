package org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.in;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;

public interface DomainEventConsumer {

    Object consume(CartItemEvent cartItemEvent);
}
