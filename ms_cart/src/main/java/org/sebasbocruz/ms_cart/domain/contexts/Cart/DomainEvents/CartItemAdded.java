package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents;

public record CartItemAdded(Long cartId, Long productId, int quantity) {
}
