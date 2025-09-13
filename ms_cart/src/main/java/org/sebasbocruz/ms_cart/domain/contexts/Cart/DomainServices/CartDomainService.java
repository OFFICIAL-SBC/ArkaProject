package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainServices;

public class CartDomainService {

    public void addWithPolicy(Cart cart, ProductId pid, int qty, StockPolicy policy) {
        if (!policy.isAvailable(pid, qty)) throw new IllegalStateException("No stock");
        cart.addItem(pid, qty);
    }

    // TODO: Revisar porque esta interface esta aqui
    public interface StockPolicy { boolean isAvailable(ProductId productId, int qty); }

}
