package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CartStateEvent {
    private Long cartId;
    private String state;

    public CartStateEvent(Long cartId, String state) {
        this.cartId = cartId;
        this.state = state;
    }
}
