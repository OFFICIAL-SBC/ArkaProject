package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children.StateEvent;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartStateEvent;

public class CartAbandoned extends CartStateEvent {
    private String reason;

    public CartAbandoned(Long cartId , String State, String reason) {
        super(cartId,State);
        this.reason = reason;
    }
}
