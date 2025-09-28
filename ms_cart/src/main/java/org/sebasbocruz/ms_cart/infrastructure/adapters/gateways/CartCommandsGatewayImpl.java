package org.sebasbocruz.ms_cart.infrastructure.adapters.gateways;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.CartItemAdded;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.CartOpened;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainServices.CartDomainService;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands.CartCommandsGateway;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.DomainEventPublisher;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartDetailEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartStateEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.common.CurrencyEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.product.ProductEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.users.UserEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CartCommandsGatewayImpl implements CartCommandsGateway {

    private Logger logger = LoggerFactory.getLogger(CartCommandsGatewayImpl.class);

    // Repositories
    private final CartRepository cartRepository;
    private final CartStateRepository cartStateRepository;
    private final UserRepository userRepository;
    private final CurrencyEntityRepository currencyEntityRepository;
    private final CartDetailEntityRespository cartDetailEntityRespository;
    private final ProductEntityRepository productEntityRepository;

    private final DomainEventPublisher publisher;
    private final CartDomainService domainService;

    @Override
    public CartDTO createNewCart(CartDTO cartDTO) {

        CartStateEntity cartStateEntity = cartStateRepository.findCartStateEntitiesByCartState(CartState.OPEN);
        UserEntity userEntity = userRepository.findUserEntitiesById(cartDTO.getUserID());
        CurrencyEntity currencyEntity = currencyEntityRepository.findCurrencyEntitiesByCode(cartDTO.getCurrencyCode());

        CartEntity cartEntity =  CartEntity.builder()
                .userEntity(userEntity)
                .cartState(cartStateEntity)
                .currencyEntity(currencyEntity)
                .build();

        CartEntity cartSaved = cartRepository.save(cartEntity);

        publisher.publish(new CartOpened(cartSaved.getId(), cartSaved.getUserEntity().getId(), cartSaved.getCurrencyEntity().getCode().name()));

        return new CartDTO(
                    cartSaved.getCurrencyEntity().getCode(),
                    0.0,
                    new ArrayList<LineDTO>(),
                    cartSaved.getUserEntity().getId()
                );
    }

    public LineDTO addItemToAnExistingCart(Long cart_id, LineDTO newLine){

        // --- Precondition checks
        Objects.requireNonNull(newLine, "newLine must not be null");
        if (newLine.getNumberOfProducts() <= 0) {
            throw new IllegalArgumentException("numberOfProducts must be > 0");
        }
        if (newLine.getProductName() == null || newLine.getProductName().isBlank()) {
            throw new IllegalArgumentException("productName must not be blank");
        }


        // --- Load cart (single query)
        CartEntity cartEntity = cartRepository.findById(cart_id)
                .orElseThrow(() -> new EntityNotFoundException("Cart " + cart_id + " not found"));


        if(!CartState.OPEN.equals(cartEntity.getCartState().getCartState())){
            throw new IllegalStateException("Cart " + cart_id + " is not OPEN");
        }

        ProductEntity productEntity = productEntityRepository.findByName(newLine.getProductName())
                .orElseThrow(() -> new EntityNotFoundException("Product '" + newLine.getProductName() + "' not found"));

        // --- Upsert cart line (avoid duplication)
        CartDetailEntity line = cartDetailEntityRespository
                .findByCartEntityAndProduct(cartEntity, productEntity)
                .map(existing -> {
                    existing.setAmount(existing.getAmount() + newLine.getNumberOfProducts());
                    return existing;
                })
                .orElseGet(() -> CartDetailEntity.builder()
                        .cartEntity(cartEntity)
                        .product(productEntity)
                        .amount(newLine.getNumberOfProducts())
                        .build());

        CartDetailEntity saved = cartDetailEntityRespository.save(line);

        // --- Publish domain/integration event
        publisher.publish(new CartItemAdded(cart_id, productEntity.getId(), newLine.getNumberOfProducts()));

        double subtotal = saved.getAmount() * productEntity.getPrice();

        return new LineDTO(
                productEntity.getName(),
                saved.getAmount(),
                subtotal
        );
    }
}
