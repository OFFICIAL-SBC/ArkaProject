package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.repositories;

import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartStateRepository extends JpaRepository<CartStateEntity,Integer> {

    CartStateEntity findCartStateEntitiesByCartState(CartState cartState);

}
