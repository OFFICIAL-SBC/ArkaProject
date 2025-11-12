package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;

public class CartOpened extends CartItemEvent {

    public CartOpened(Long cartId, Long userId, String currency){
        super(cartId, userId, currency);
    }
}
