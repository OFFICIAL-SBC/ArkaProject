package org.sebasbocruz.ms_cart.infrastructure.entrypoints.controllers.commands;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.application.command.CreateCartUseCase;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cart/v1/cart")
@RequiredArgsConstructor
public class CartCommandController {

    private final CreateCartUseCase createCartUseCase;


    @PostMapping("/create")
    public ResponseEntity<CartDTO> createNewCart(@RequestBody CartDTO cartRequest){
        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(createCartUseCase.createCart(cartRequest));
    }


}
