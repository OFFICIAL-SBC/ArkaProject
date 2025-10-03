package org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainServices;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate.Cart;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.StockPolicy;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductName;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class CartDomainService {

        private final StockPolicy stockPolicy;

        public void addWithPolicy(Cart cart, ProductId product_id, ProductName productName, int quantity, ProductPrice price) {
            if (!stockPolicy.isAvailable(product_id, quantity)) throw new IllegalStateException("No stock AVAILABLE of product with ID"+ product_id);
            cart.addItem(product_id,quantity, productName,price);
       }

}
