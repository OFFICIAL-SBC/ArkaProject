package org.sebasbocruz.ms_cart.application.command;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate.Cart;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainServices.CartDomainService;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartId;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands.CartCommandsGateway;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.DomainEventPublisher;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.sebasbocruz.ms_cart.domain.contexts.Product.entities.Product;
import org.sebasbocruz.ms_cart.domain.contexts.Product.gateway.CatalogQuery;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;


@RequiredArgsConstructor
public class ChangeItemQuantityUseCase {
    private final CartCommandsGateway cartCommandsGateway;
    private final CatalogQuery catalogQuery;
    private final CartDomainService domainService;
    private final DomainEventPublisher publisher;

    public LineDTO changeItemQuantity (Long cart_id, Long product_id, int product_quantity){

        Cart cartFound = cartCommandsGateway.findCartById(new CartId(cart_id)).orElseThrow(
                () -> new EntityNotFoundException("The cart with ID "+cart_id+" Does not exist in the Database")
        );

        Product foundProduct = catalogQuery.getProduct(product_id)
                .orElseThrow(() -> new EntityNotFoundException("The PRODUCT with ID "+product_id+" Does not exist in the Database"));

        if(cartFound.getLines().get(new ProductId(product_id)) == null){
            throw new EntityNotFoundException("The Product with ID "+product_id+" is not in the user's cart");
        }

        domainService.changeItemQuantityWithPolicy(
                cartFound,
                new ProductId(product_id),
                cartFound.getLines().get(new ProductId(product_id)).quantity(),
                product_quantity,
                foundProduct.getPrice()
        );

        Cart cartSaved = cartCommandsGateway.save(cartFound);

        cartFound.pullCartItemEvents().forEach(publisher::publishCartItemEvent);


        return new LineDTO(
            product_id,
            foundProduct.getName().name(),
            cartSaved.getLines().get(new ProductId(product_id)).quantity(),
            cartSaved.getLines().get(new ProductId(product_id)).subtotal()
        );
    }

}
