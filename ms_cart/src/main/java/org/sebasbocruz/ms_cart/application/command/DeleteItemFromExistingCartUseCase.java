package org.sebasbocruz.ms_cart.application.command;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate.Cart;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartId;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartLine;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands.CartCommandsGateway;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.DomainEventPublisher;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
public class DeleteItemFromExistingCartUseCase {

    private final CartCommandsGateway cartCommandsGateway;
    private final DomainEventPublisher publisher;

    public LineDTO deleteItemFromExistingCart(
            long cart_id,
            long product_id
    ){
        Cart cartFound = cartCommandsGateway.findCartById(new CartId(cart_id)).orElseThrow(
                () -> new EntityNotFoundException("The cart with ID "+cart_id+"Does not exist")
        );

        CartLine cartLineDeleted = cartFound.removeItem(new ProductId(product_id));

        cartCommandsGateway.save(cartFound);

        cartFound.getDomainEvents().forEach(publisher::publish);

        return new LineDTO(
                product_id,
                cartLineDeleted.productName().name(),
                cartLineDeleted.quantity(),
                cartLineDeleted.subtotal()
        );

    }

}
