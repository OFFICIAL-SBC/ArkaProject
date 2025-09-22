package org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.in;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.CartOpened;

public interface DomainEventConsumer {

    Object consume(CartOpened cartOpened);
}
