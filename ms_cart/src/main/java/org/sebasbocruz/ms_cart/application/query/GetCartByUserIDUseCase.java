package org.sebasbocruz.ms_cart.application.query;


import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.query.GetProductsGateway;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;

import javax.swing.text.html.parser.Entity;
import java.util.Optional;


public class GetCartByUserIDUseCase {

    private final GetProductsGateway getProductsGateway;

    private GetCartByUserIDUseCase(GetProductsGateway getProductsGateway){
        this.getProductsGateway = getProductsGateway;
    }

    public Optional<CartEntity> getCartByUserID(Long userId){
        return getProductsGateway.getCartByUserID(userId);
    }

}
