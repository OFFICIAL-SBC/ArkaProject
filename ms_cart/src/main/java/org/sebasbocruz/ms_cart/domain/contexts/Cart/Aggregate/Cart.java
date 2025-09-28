package org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate;

import lombok.Getter;
import lombok.Setter;
import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.commons.enums.CurrencyCode;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.*;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.CartLine;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.CartId;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.UserId;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductName;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductPrice;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Cart {

    private final CartId id;
    private final UserId userId;
    private CartState state;
    private CurrencyCode currency;
    private final LinkedHashMap<ProductId, CartLine> lines = new LinkedHashMap<>();
    private final List<Object> domainEvents = new ArrayList<>();


    public Cart(CartId id, UserId userId, CurrencyCode currency, CartState state, Map<ProductId, CartLine> lines) {
        this.id = id;
        this.userId = userId;
        this.currency = currency;
        this.state = state;
        this.lines.putAll(lines);
    }


    public void addItem(ProductId productId, int quantity, ProductName name, ProductPrice price) {
        ensureCartIsOpen();
        if (quantity <= 0) throw new IllegalArgumentException("qty > 0");
        lines.merge(productId, new CartLine(name, quantity, quantity*price.value()),
                (oldLine, newLine) -> {
                    int newAmount = oldLine.quantity() + newLine.quantity();
                    return oldLine.withQuantity(newAmount, newAmount*price.value());
                });
        domainEvents.add(new CartItemAdded(id.value(), productId.value(), quantity));
    }

    public void changeItemQuantity(ProductId productId, int quantity, ProductPrice price) {
        ensureCartIsOpen();
        if (!lines.containsKey(productId)) throw new IllegalStateException("Item not in cart");
        lines.put(productId, lines.get(productId).withQuantity(quantity, quantity*price.value()));
        domainEvents.add(new CartItemQuantityChanged(id.value(), productId.value(), quantity));
    }

    public void removeItem(ProductId productId) {
        ensureCartIsOpen();
        if (lines.remove(productId) != null) domainEvents.add(new CartItemRemoved(id.value(), productId.value()));
    }

    public void cancel(String reason) {
        ensureCartIsOpen();
        this.state = CartState.CANCELLED;
        domainEvents.add(new CartCancelled(id.value(), reason));
    }

    public void convertToOrder(String orderId) {
        ensureCartIsOpen();
        this.state = CartState.CONVERTED;
        domainEvents.add(new CartConverted(id.value(), orderId));
    }

    private void ensureCartIsOpen() {
        if (state != CartState.OPEN) throw new IllegalStateException("Cart is not OPEN");
    }

    public List<Object> pullDomainEvents() {
        var copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }
}
