package org.sebasbocruz.ms_cart.infrastructure.adapters.gateways.product;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.StockPolicy;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.springframework.stereotype.Service;

@Service
public class StockPolicyImpl implements StockPolicy {

    @Override
    public boolean isAvailable(ProductId productId, int quantity) {
        return true;
    }

}
