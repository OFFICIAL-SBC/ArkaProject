package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.repositories;

import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartDetailEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.cart.CartEntity;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartDetailEntityRespository extends JpaRepository<CartDetailEntity, Long> {

    Optional<CartDetailEntity> findByCartEntityAndProduct(CartEntity cartEntity, ProductEntity product);

}
