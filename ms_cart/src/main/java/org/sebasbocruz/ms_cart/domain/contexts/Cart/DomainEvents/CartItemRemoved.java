package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents;

public record CartItemRemoved(Long cartId, Long productId) {
}
