package org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.in;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children.CartOpened;

public interface DomainEventConsumer {

    Object consume(CartItemEvent cartItemEvent);
}
