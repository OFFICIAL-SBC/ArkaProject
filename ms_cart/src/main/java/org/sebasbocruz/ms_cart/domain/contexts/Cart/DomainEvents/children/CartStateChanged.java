package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;

public class CartStateChanged extends CartItemEvent {

    protected CartStateChanged(Long cartId) {
        super(cartId);
    }
}
