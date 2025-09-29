package org.sebasbocruz.ms_cart.infrastructure.entrypoints.controllers.commands;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.application.command.AddItemToExistingCartUseCase;
import org.sebasbocruz.ms_cart.application.command.AddItemToExistingCartUseCase;
import org.sebasbocruz.ms_cart.application.command.CreateCartUseCase;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.Line;
import java.util.Optional;

@RestController
@RequestMapping("cart/v1")
@RequiredArgsConstructor
public class CartCommandController {

    private final CreateCartUseCase createCartUseCase;
    private final AddItemToExistingCartUseCase addItemToExistingCartUseCase;


    @PostMapping("/create")
    public ResponseEntity<CartDTO> createNewCart(@RequestBody CartDTO cartRequest){
        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(createCartUseCase.createCart(cartRequest));
    }

    @PostMapping("/item/{cart_id}")
    public ResponseEntity<LineDTO> addNewItemToAExistingCart(@PathVariable Long cart_id, @RequestBody LineDTO line) {
        LineDTO lineAdded = addItemToExistingCartUseCase.addItemToExistingCart(cart_id,line);
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(lineAdded);
    }


}
