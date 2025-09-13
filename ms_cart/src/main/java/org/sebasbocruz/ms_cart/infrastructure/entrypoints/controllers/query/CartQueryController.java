package org.sebasbocruz.ms_cart.infrastructure.entrypoints.controllers.query;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.application.query.GetCartByUserIDUseCase;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("cart/v1/products")
@RequiredArgsConstructor
public class CartQueryController {

    private final GetCartByUserIDUseCase getCartByUserIDUseCase;

    @GetMapping
    public ResponseEntity<Optional<CartEntity>> getCartByUserID(@RequestParam Long userID){
        Optional<CartEntity> result = getCartByUserIDUseCase.getCartByUserID(userID);

        if(result.isEmpty()) throw  new EntityNotFoundException("No CART OPEN found for the USER");


        return ResponseEntity.ok(result);

    }

}
