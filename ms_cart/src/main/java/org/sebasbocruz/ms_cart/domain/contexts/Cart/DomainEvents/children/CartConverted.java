package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;

public class CartConverted extends CartItemEvent{
    public CartConverted(Long cartId, String orderId){
        super(cartId,orderId);
    }
}
