package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;

public class CartCancelled extends CartItemEvent {
    public CartCancelled(Long cartId, String reason) {
        super(cartId, reason);
    }
}
