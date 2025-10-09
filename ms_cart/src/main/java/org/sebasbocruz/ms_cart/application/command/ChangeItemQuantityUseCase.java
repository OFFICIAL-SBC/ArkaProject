package org.sebasbocruz.ms_cart.application.command;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate.Cart;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainServices.CartDomainService;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartId;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands.CartCommandsGateway;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductName;
import org.sebasbocruz.ms_cart.domain.contexts.Product.entities.Product;
import org.sebasbocruz.ms_cart.domain.contexts.Product.gateway.CatalogQuery;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.QueryProductDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
public class ChangeItemQuantityUseCase {
    private final CartCommandsGateway cartCommandsGateway;
    private final CatalogQuery catalogQuery;
    private final CartDomainService domainService;

    public LineDTO changeItemQuantity (Long cart_id, Long product_id, int product_quantity){

        Cart cartFound = cartCommandsGateway.finCartById(new CartId(cart_id)).orElseThrow(
                () -> new EntityNotFoundException("The cart with ID "+cart_id+" Does not exist")
        );



        Product foundProduct = catalogQuery.getProduct(product_id)
                .orElseThrow(() -> new EntityNotFoundException("The PRODUCT with ID "+product_id+" Does not exist"));


        // Here what should I do? I need to validate that if add so many products
        // I'm not adding more products that what I have in inventory
        domainService.changeItemQuantityWithPolicy(
                cartFound,
                new ProductId(product_id),
                product_quantity,
                foundProduct.getPrice()
        );


    }

}
