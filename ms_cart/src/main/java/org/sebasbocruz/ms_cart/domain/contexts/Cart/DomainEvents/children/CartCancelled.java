package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartStateEvent;

public class CartCancelled extends CartStateEvent {

    private String reason;

    public CartCancelled(Long cartId, String state, String reason) {
        super(cartId, state);
        this.reason = reason;
    }
}
