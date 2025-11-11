package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;

public class CartItemQuantityChanged extends CartItemEvent {

    public CartItemQuantityChanged(Long cartId, Long productId, int quantity){
        super();
        this.setCartId(cartId);
        this.setProductId(productId);
        this.setQuantity(quantity);
    }
}
