package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.repositories;

import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartStateRepository extends JpaRepository<CartStateEntity,Integer> {

    Optional<CartStateEntity> findCartStateEntitiesByCartState(CartState cartState);

}
