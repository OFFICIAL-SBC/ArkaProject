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

       public void changeItemQuantityWithPolicy(Cart cart, ProductId product_id, int currentQuantity, int newQuantity, ProductPrice price){
            // ! The logic is this:
           // ! I always subtract the amount I send through events to the inventory microservice
           // ! If newQuantity > currentQuantity then the subtraction will be positive, meaning that I only need to
           // ! check if the inventory has available the difference between those 2 variables
           // ! If the subtraction is negative -> It means that there is more items in cart that what I actually need,
           // ! so we should "RETURN" those remaining products to the inventory. Now, since I'm always subtracting the amount
           // ! sent though events, in this case this amount will be negative, the final result will be
           // ! inventory = currentAmountInventory - (newQuantity - currentQuantity)

            int quantityThatWeWillSubtractsFromTheInventory = newQuantity - currentQuantity;
            int quantity_validation = Math.max(quantityThatWeWillSubtractsFromTheInventory, 0);
           if (!stockPolicy.isAvailable(product_id, quantity_validation)) throw new IllegalStateException("No stock AVAILABLE of product with ID"+ product_id);
           cart.changeItemQuantity(product_id,currentQuantity,newQuantity,price);
        }

}
