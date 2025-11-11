package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CartItemEvent {
    private Long cartId;
    private Long productId;
    private int quantity;
}
