package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents;

public record CartCancelled(Long cartId, String reason) {
}
