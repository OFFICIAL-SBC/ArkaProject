package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children;

public record CartCancelled(Long cartId, String reason) {
}
