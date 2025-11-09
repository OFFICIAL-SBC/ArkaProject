package org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.DomainEvents.children;


import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.DomainEvents.parents.CartItemEvent;

public record CartItemQuantityChanged(Long cartId, Long productId, int quantity) implements CartItemEvent {
}
