package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartStateEvent;

public class CartConverted extends CartStateEvent {
    public CartConverted(Long cartId, String state) {
        super(cartId, state);
    }
}
