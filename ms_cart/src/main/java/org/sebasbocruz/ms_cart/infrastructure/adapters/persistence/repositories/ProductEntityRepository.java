package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.repositories;

import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductEntityRepository extends JpaRepository<ProductEntity,Long> {
    Optional<ProductEntity> findByName(String name);
}
