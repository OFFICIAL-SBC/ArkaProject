package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents;

public record CartOpened(Long cartId, Long userId, String currency) {
}
