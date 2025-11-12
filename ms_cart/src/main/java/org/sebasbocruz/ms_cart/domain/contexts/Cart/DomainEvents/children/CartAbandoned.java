package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;

public class CartAbandoned extends CartItemEvent {
    public CartAbandoned(Long cartId , String reason) {
        super(cartId,reason);
    }
}
