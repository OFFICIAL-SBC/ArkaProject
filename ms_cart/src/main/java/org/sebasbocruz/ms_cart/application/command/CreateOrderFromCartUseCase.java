package org.sebasbocruz.ms_cart.application.command;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate.Cart;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartId;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartLine;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands.CartCommandsGateway;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.DomainEventPublisher;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CreateOrderFromCartUseCase {
    private final CartCommandsGateway cartCommandsGateway;
    private final DomainEventPublisher domainEventPublisher;

    public CartDTO convertCartToOrder(Long cartId){
        Cart cart = cartCommandsGateway.findCartById(new CartId(cartId))
                .orElseThrow(() -> new IllegalArgumentException("Cart not found in the database"));

        cart.convertToOrder();

        // ! Since I save this cart, and in my database model I dont care about the events
        // ! This cartSaved will not have the events
        // ! So I need to publish the events from the cart before saving it
        Cart cartSaved = cartCommandsGateway.save(cart);

        cart.pullCartStateEvents().forEach(domainEventPublisher::publishCartStateEvent);

        List<LineDTO> cartLines = new ArrayList<>();

        double total = 0.0;
        for(Map.Entry<ProductId, CartLine> itemTuple: cartSaved.getLines().entrySet()){
            total += itemTuple.getValue().subtotal();
            cartLines.add(new LineDTO(
                    itemTuple.getKey().value(),
                    itemTuple.getValue().productName().name(),
                    itemTuple.getValue().quantity(),
                    itemTuple.getValue().subtotal()
            ));
        }



        return new CartDTO(cartSaved.getUserId().value(),cartSaved.getState(),cartSaved.getCurrency(),total,cartLines);

    }

}
