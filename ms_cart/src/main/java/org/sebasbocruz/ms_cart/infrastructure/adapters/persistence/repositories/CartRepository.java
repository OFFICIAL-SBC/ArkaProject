package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.repositories;

import org.sebasbocruz.ms_cart.domain.commons.enums.CartState;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.UserId;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartStateEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity,Long> {


    Optional<CartEntity> findByUserEntity_IdAndCartState_CartState(Long userEntity_id, CartState cartState);

    CartEntity findCartEntitiesById(Long id);
}
