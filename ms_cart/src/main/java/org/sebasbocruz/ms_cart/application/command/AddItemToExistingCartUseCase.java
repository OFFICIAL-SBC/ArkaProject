package org.sebasbocruz.ms_cart.application.command;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands.CartCommandsGateway;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;

import java.util.Optional;

@RequiredArgsConstructor
public class AddItemToExistingCartUseCase {

    private final CartCommandsGateway cartCommandsGateway;


    public LineDTO addItemToExistingCart(Long cart_id, LineDTO newLine){
        return cartCommandsGateway.addItemToAnExistingCart(cart_id, newLine);
    }



}
