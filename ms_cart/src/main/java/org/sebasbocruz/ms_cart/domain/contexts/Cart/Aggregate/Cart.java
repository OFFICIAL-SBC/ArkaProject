package org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate;

import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.commons.enums.CurrencyCode;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.CartCancelled;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.CartConverted;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.CartItemAdded;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.CartOpened;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Cart {

    private final CartId id;
    private final UserId userId;
    private CartState state;
    private CurrencyCode currency;
    private final LinkedHashMap<ProductId, CartLine> lines = new LinkedHashMap<>();
    private final List<Object> domainEvents = new ArrayList<>();

    private Cart(CartId id, UserId userId, CurrencyCode currency) {
        this.id = id; this.userId = userId; this.currency = currency;
        this.state = CartState.OPEN;
        domainEvents.add(new CartOpened(id.value(), userId.value(), currency.name()));
    }
    public static Cart open(CartId id, UserId userId, CurrencyCode currency) { return new Cart(id, userId, currency); }

    public void addItem(ProductId productId, int quantity) {
        ensureOpen();
        if (quantity <= 0) throw new IllegalArgumentException("qty > 0");
        lines.merge(productId, new CartLine(productId, quantity),
                (oldL, newL) -> oldL.withQuantity(oldL.quantity() + newL.quantity()));
        domainEvents.add(new CartItemAdded(id.value(), productId.value(), quantity));
    }

    public void changeItemQuantity(ProductId productId, int quantity) {
        ensureOpen();
        if (!lines.containsKey(productId)) throw new IllegalStateException("Item not in cart");
        lines.put(productId, lines.get(productId).withQuantity(quantity));
        domainEvents.add(new CartItemQuantityChanged(id.value(), productId.value(), quantity));
    }

    public void removeItem(ProductId productId) {
        ensureOpen();
        if (lines.remove(productId) != null) domainEvents.add(new CartItemRemoved(id.value(), productId.value()));
    }

    public void cancel(String reason) {
        ensureOpen();
        this.state = CartState.CANCELLED;
        domainEvents.add(new CartCancelled(id.value(), reason));
    }

    public void convertToOrder(String orderId) {
        ensureOpen();
        this.state = CartState.CONVERTED;
        domainEvents.add(new CartConverted(id.value(), orderId));
    }

    private void ensureOpen() {
        if (state != CartState.OPEN) throw new IllegalStateException("Cart not OPEN");
    }

    public List<Object> pullDomainEvents() {
        var copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }
}
