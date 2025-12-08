package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children.ItemEvent;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;

public class CartItemRemoved extends CartItemEvent {

    public CartItemRemoved(Long cartId, Long productId, int quantity){
        super(cartId, productId, quantity);
    }

}
