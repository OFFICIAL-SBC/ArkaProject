package org.sebasbocruz.ms_cart.infrastructure.entrypoints.controllers.commands;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.application.command.*;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.QueryProductDTO;
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
    private final DeleteItemFromExistingCartUseCase deleteItemsFromExistingCartUseCase;
    private final ChangeItemQuantityUseCase changeItemQuantityUseCase;
    private final CreateOrderFromCartUseCase createOrderFromCartUseCase;


    @PostMapping("/create")
    public ResponseEntity<CartDTO> createNewCart(@RequestBody CartDTO cartRequest){
        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(createCartUseCase.createCart(cartRequest));
    }

    @PostMapping("/item/{cart_id}")
    public ResponseEntity<LineDTO> addNewItemsToAExistingCart(@PathVariable Long cart_id, @RequestBody LineDTO line) {
        LineDTO lineAdded = addItemToExistingCartUseCase.addItemToExistingCart(cart_id,line);
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(lineAdded);
    }

    @DeleteMapping("/item/{cart_id}")
        public ResponseEntity<LineDTO> deleteItemFromAnExistingCart(@PathVariable Long cart_id, @RequestParam long product_id){

            LineDTO lineDTODeleted = deleteItemsFromExistingCartUseCase.deleteItemFromExistingCart(cart_id,product_id);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(lineDTODeleted);
    }

    @PutMapping("/item/{cart_id}")
    public ResponseEntity<LineDTO> changeItemQuantity(@PathVariable Long cart_id, @RequestBody QueryProductDTO productData){
        return ResponseEntity.status(HttpStatus.OK)
                .body(changeItemQuantityUseCase.changeItemQuantity(cart_id,productData.getProduct_id(),productData.getQuantity()));
    }

    @PutMapping("/converted/{cart_id}")
    public ResponseEntity<CartDTO> convertCartToOrder(@PathVariable Long cart_id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(createOrderFromCartUseCase.convertCartToOrder(cart_id));
    }

}
