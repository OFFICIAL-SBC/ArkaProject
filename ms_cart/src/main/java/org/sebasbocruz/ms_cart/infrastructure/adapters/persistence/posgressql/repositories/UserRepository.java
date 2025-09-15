package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.repositories;

import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findUserEntitiesById(Long id);
}
