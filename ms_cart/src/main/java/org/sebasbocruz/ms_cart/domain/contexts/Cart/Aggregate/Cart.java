package org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.commons.enums.CurrencyCode;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartStateEvent;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children.ItemEvent.CartItemAdded;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children.ItemEvent.CartItemQuantityChanged;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children.ItemEvent.CartItemRemoved;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children.StateEvent.CartAbandoned;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children.StateEvent.CartCancelled;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children.StateEvent.CartConverted;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartLine;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartId;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.UserId;
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
    private final List<CartItemEvent> cartItemEvents = new ArrayList<>();
    private final List<CartStateEvent> cartStateEvents = new ArrayList<>();


    public Cart(CartId id, UserId userId, CurrencyCode currency, CartState state, Map<ProductId, CartLine> lines) {
        this.id = id;
        this.userId = userId;
        this.currency = currency;
        this.state = state;
        this.lines.putAll(lines);
    }


    public void addItem(ProductId productId, int quantity, ProductName name, ProductPrice price) {
        ensureCartIsOpen();
        lines.merge(productId,
                new CartLine(name, quantity, quantity*price.value()),
                (oldLine, newLine) -> {
                    int newAmount = oldLine.quantity() + newLine.quantity();
                    return oldLine.withQuantity(newAmount, newAmount*price.value());
                });
        cartItemEvents.add(new CartItemAdded(id.value(), productId.value(), quantity));
    }

    public void changeItemQuantity(ProductId productId,int currentQuantity, int newQuantity, ProductPrice price) {
        ensureCartIsOpen();
        if (!lines.containsKey(productId)) throw new IllegalStateException("Item not in cart");
        lines.put(productId, lines.get(productId).withQuantity(newQuantity, newQuantity*price.value()));
        cartItemEvents.add(new CartItemQuantityChanged(id.value(), productId.value(), newQuantity - currentQuantity));
    }

    public CartLine removeItem(ProductId productId) {
        ensureCartIsOpen();
        CartLine cartLineRemoved = lines.remove(productId);
            if (cartLineRemoved != null) {
                cartItemEvents.add(new CartItemRemoved(id.value(), productId.value(),cartLineRemoved.quantity()));
            }else {
                throw new EntityNotFoundException("The product with ID "+productId.value()+" is not in the CART");
            }

            return cartLineRemoved;
    };

    public void cancel(String reason) {
        ensureCartIsOpen();
        this.state = CartState.CANCELLED;
        cartStateEvents.add(new CartCancelled(id.value(),CartState.CANCELLED.name(), reason));
    }

    public void abandoned(String reason) {
        ensureCartIsOpen();
        this.state = CartState.ABANDONED;
        cartStateEvents.add(new CartAbandoned(id.value(), CartState.ABANDONED.name(),reason));
    }

    public void convertToOrder() {
        ensureCartIsOpen();
        this.state = CartState.CONVERTED;
        cartStateEvents.add(new CartConverted(id.value(),CartState.CONVERTED.name()));
    }

    private void ensureCartIsOpen() {
        if (state != CartState.OPEN) throw new IllegalStateException("Cart is not OPEN");
    }

    public List<CartItemEvent> pullCartItemEvents() {
        var copy = List.copyOf(cartItemEvents);
        cartItemEvents.clear();
        return copy;
    }

    public List<CartStateEvent> pullCartStateEvents() {
        var copy = List.copyOf(cartStateEvents);
        cartStateEvents.clear();
        return copy;
    }
}
