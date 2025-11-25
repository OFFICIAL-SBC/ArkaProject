package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartStateEvent;

public class CartOpened extends CartStateEvent {

    private Long userId;
    private String currency;

    public CartOpened(Long cartId, String state, Long userId, String currency) {
        super(cartId,state);
        this.userId = userId;
        this.currency = currency;
    }
}
