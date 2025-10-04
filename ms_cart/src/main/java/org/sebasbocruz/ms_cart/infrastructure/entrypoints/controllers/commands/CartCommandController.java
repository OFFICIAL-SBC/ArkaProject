package org.sebasbocruz.ms_cart.infrastructure.entrypoints.controllers.commands;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.application.command.AddItemToExistingCartUseCase;
import org.sebasbocruz.ms_cart.application.command.CreateCartUseCase;
import org.sebasbocruz.ms_cart.application.command.DeleteItemFromExistingCartUseCase;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.Line;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("cart/v1")
@RequiredArgsConstructor
public class CartCommandController {

    private final CreateCartUseCase createCartUseCase;
    private final AddItemToExistingCartUseCase addItemToExistingCartUseCase;
    private final DeleteItemFromExistingCartUseCase deleteItemsFromExistingCart;


    @PostMapping("/create")
    public ResponseEntity<CartDTO> createNewCart(@RequestBody CartDTO cartRequest){
        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(createCartUseCase.createCart(cartRequest));
    }

    @PostMapping("/item/{cart_id}")
    public ResponseEntity<List<LineDTO>> addNewItemsToAExistingCart(@PathVariable Long cart_id, @RequestBody List<LineDTO> lines) {
        List<LineDTO> lineAdded = addItemToExistingCartUseCase.addItemsToExistingCart(cart_id,lines);
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(lineAdded);
    }

    @DeleteMapping("/item/{cart_id}")
    public ResponseEntity<CartDTO> deleteItemFromAnExistingCart(@PathVariable Long cart_id, @RequestParam long product_id){

    }



}
