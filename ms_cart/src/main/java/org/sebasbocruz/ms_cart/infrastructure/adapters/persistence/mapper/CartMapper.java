package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.mapper;

import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.commons.enums.CurrencyCode;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate.Cart;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartId;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartLine;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.UserId;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductName;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartDetailEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.product.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public final class CartMapper {

    public static CartDTO fromInfrastructureToClient(CartEntity entity){

        double accumulator = 0.0;
        List<LineDTO> lines = new ArrayList<LineDTO>();

        for(var detailEntity : entity.getDetails()){
            String name = detailEntity.getProduct().getName();
            int amount = detailEntity.getAmount();
            double subtotal = detailEntity.getAmount()*detailEntity.getProduct().getPrice();
            accumulator += subtotal;
            lines.add(new LineDTO(detailEntity.getId(),name,amount,subtotal));
        }

        return new CartDTO(entity.getUserEntity().getId(),entity.getCurrencyEntity().getCode(),accumulator,lines);
    }

    public static Cart fromInfrastructureToDomain(CartEntity entity) {

        var cart_id = new CartId(entity.getId());
        var userId = new UserId(entity.getUserEntity().getId());
        var currency = CurrencyCode.valueOf(entity.getCurrencyEntity().getCode().name());
        var state = switch (entity.getCartState().getCartState()) {
            case OPEN -> CartState.OPEN;
            case INACTIVE -> CartState.INACTIVE;
            case ABANDONED -> CartState.ABANDONED;
            case CANCELLED -> CartState.CANCELLED;
            case CONVERTED -> CartState.CONVERTED;
        };
        Map<ProductId, CartLine> lines = new LinkedHashMap<>();
        for (var cartDetail : entity.getDetails()) {
            ProductId pid = new ProductId(cartDetail.getProduct().getId());
            lines.put(
                    pid,
                    new CartLine(new ProductName(cartDetail.getProduct().getName()), cartDetail.getAmount(),cartDetail.getProduct().getPrice()*cartDetail.getAmount())
            );
        }
        return new Cart(cart_id, userId, currency, state, lines);
    }

    public static void synchronizeJpaEntityWithDomain(Cart domainCart, CartEntity cartEntity, Function<Long, ProductEntity> productById) {

        Map<Long, Integer> desiredCartDetailListDomain = domainCart.getLines().entrySet().stream()
                .collect(Collectors.toMap(setKeyValue -> setKeyValue.getKey().value(), setKeyValue -> setKeyValue.getValue().quantity()));

        Iterator<CartDetailEntity> cartDetailEntityIterator = cartEntity.getDetails().iterator();

        while (cartDetailEntityIterator.hasNext()) {
            CartDetailEntity cartDetailEntityNext = cartDetailEntityIterator.next();
            Long product_id = cartDetailEntityNext.getProduct().getId();
            if (!desiredCartDetailListDomain.containsKey(product_id)) {
                cartDetailEntityIterator.remove(); // orphanRemoval -> DELETE
            } else {
                cartDetailEntityNext.setAmount(desiredCartDetailListDomain.get(product_id));
                desiredCartDetailListDomain.remove(product_id);
            }
        }

        for (var entry : desiredCartDetailListDomain.entrySet()) {
            CartDetailEntity productsThatArentInTheEntityBuyTheyAreInTheDomain = new CartDetailEntity();
            productsThatArentInTheEntityBuyTheyAreInTheDomain.setCartEntity(cartEntity);
            productsThatArentInTheEntityBuyTheyAreInTheDomain.setProduct(productById.apply(entry.getKey()));
            productsThatArentInTheEntityBuyTheyAreInTheDomain.setAmount(entry.getValue());
            cartEntity.getDetails().add(productsThatArentInTheEntityBuyTheyAreInTheDomain);
        }
    }



}
