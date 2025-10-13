package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children;

public record CartOpened(Long cartId, Long userId, String currency) {
}
