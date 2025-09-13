package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.mapper;

import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.commons.enums.CurrencyCode;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate.Cart;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.CartId;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.CartLine;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.UserId;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductName;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductPrice;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public final class CartMapper {

    public static Cart toDomain(CartEntity entity) {
        CartId id       = new CartId(entity.getId());
        UserId userId   = new UserId(entity.getUserEntity().getId());
        CurrencyCode currency = entity.getCurrencyEntity().getCode();
        CartState state    = entity.getCartState().getCartState();

        Map<ProductId, CartLine> lines = new LinkedHashMap<>();

        for (var detailEntity : entity.getDetails()) {
            ProductId productId = new ProductId(detailEntity.getProduct().getId());
            ProductName productName = new ProductName(detailEntity.getProduct().getName());
            ProductPrice productPrice = new ProductPrice(detailEntity.getProduct().getPrice());
            int numberOfProducts = detailEntity.getAmount();
            lines.put(productId, new CartLine(productName, numberOfProducts, productPrice.value()*numberOfProducts));
        }

        return Cart.rehydrate(id, userId, currency, state, lines);
    }
}
