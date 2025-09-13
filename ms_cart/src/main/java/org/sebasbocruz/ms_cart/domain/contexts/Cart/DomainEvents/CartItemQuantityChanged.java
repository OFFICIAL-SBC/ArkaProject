package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents;

public record CartItemQuantityChanged(Long cartId, Long productId, int quantity) {
}
