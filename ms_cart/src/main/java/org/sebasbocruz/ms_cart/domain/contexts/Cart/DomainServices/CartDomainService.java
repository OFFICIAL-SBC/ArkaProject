package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainServices;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate.Cart;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.StockPolicy;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.springframework.stereotype.Component;

@Component
public class CartDomainService {

        public void addWithPolicy(Cart cart, ProductId pid, int qty, StockPolicy policy) {
        if (!policy.isAvailable(pid, qty)) throw new IllegalStateException("No stock");
    }

}
