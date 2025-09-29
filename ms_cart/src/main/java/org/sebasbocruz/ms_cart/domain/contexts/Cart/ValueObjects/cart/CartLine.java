package org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart;

import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductName;

// ! The canonical constructor is the one that receives all the components of the record object in the same order in which
// ! they were declared
public record CartLine(ProductName productName, int quantity, double subtotal) {

    // ! This is the compact constructor, a SHORTHAND WAY of redefining the canonical constructor.
    // ! The params assignations it is made IMPLICITLY AT THE END OF THE BLOCK
    public CartLine {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than  0");
    }

    public CartLine withQuantity(int q, double subtotal) {
        return new CartLine(productName, q, subtotal);
    }
}
