package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.gateways;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.CartOpened;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainServices.CartDomainService;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands.CartCommandsGateway;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.DomainEventPublisher;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.CartDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartStateEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.common.CurrencyEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.users.UserEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.repositories.CartRepository;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.repositories.CartStateRepository;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.repositories.CurrencyEntityRepository;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartCommandsGatewayImpl implements CartCommandsGateway {

    private final CartRepository cartRepository;
    private final CartStateRepository cartStateRepository;
    private final UserRepository userRepository;
    private final CurrencyEntityRepository currencyEntityRepository;
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
}
