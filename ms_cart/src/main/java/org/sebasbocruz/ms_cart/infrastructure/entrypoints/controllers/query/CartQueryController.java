package org.sebasbocruz.ms_cart.infrastructure.entrypoints.controllers.query;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.application.query.GetCartByUserIDUseCase;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("cart/v1/products")
@RequiredArgsConstructor
public class CartQueryController {

    private final GetCartByUserIDUseCase getCartByUserIDUseCase;

    @GetMapping
    public ResponseEntity<Optional<CartDTO>> getOpenedCartByUserID(@RequestParam Long userID){
        Optional<CartDTO> result = getCartByUserIDUseCase.getOpenedCartByUserID(userID);

        if(result.isEmpty()) throw new EntityNotFoundException("There is no an Open Cart For the User");

        return ResponseEntity
                .ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(result);

    }

}
