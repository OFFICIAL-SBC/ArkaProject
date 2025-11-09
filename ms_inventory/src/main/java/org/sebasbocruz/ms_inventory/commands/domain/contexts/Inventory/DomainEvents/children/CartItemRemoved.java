package org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.DomainEvents.children;

import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.DomainEvents.parents.CartItemEvent;

public record CartItemRemoved(Long cartId, Long productId, int amount) implements CartItemEvent {
}
