package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;

public record CartItemQuantityChanged(Long cartId, Long productId, int quantity) implements CartItemEvent  {
}
