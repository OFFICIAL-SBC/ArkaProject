package org.sebasbocruz.ms_cart.application.command;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate.Cart;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands.CartCommandsGateway;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CreateCartUseCase {

    private final CartCommandsGateway cartCommandsGateway;

    public CartDTO createCart(CartDTO carDTO){

        Optional<Cart> cart = cartCommandsGateway.findCartByUserIdAndState(carDTO.getUserID(), CartState.OPEN);

        if(cart.isPresent()){
            throw new IllegalArgumentException("You can NOT create a new car since the user already has a car created");
        }

        Cart cartSaved = cartCommandsGateway.createNewCart(carDTO);

        List<LineDTO> cartLines = new ArrayList<LineDTO>();
        double total = 0.0;
        for(var itemTuple: cartSaved.getLines().entrySet()){
            total += itemTuple.getValue().subtotal();
            cartLines.add(new LineDTO(
                    itemTuple.getKey().value(),
                    itemTuple.getValue().productName().name(),
                    itemTuple.getValue().quantity(),
                    itemTuple.getValue().subtotal()
            ));
        }



        return new CartDTO(cartSaved.getUserId().value(),cartSaved.getCurrency(),total,cartLines);
    }

}
