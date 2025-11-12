package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CartItemEvent {
    private Long userId;
    private Long cartId;
    private Long productId;
    private int quantity;
    private String reason;


    protected CartItemEvent(Long cartId, Long productId, int quantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }

    protected CartItemEvent(Long cartId){
        this.cartId = cartId;
    }

    protected CartItemEvent(Long cartId, String reason){
        this.cartId = cartId;
        this.reason = reason;
    }

    protected CartItemEvent(Long cartId, Long userId, String currency){
        this.cartId = cartId;
        this.userId = userId;
        this.reason = currency;
    }

}
