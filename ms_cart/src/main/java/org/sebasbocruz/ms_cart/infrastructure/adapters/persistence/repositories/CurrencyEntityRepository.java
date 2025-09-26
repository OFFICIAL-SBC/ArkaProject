package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.repositories;

import org.sebasbocruz.ms_cart.domain.commons.enums.CurrencyCode;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.common.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyEntityRepository extends JpaRepository<CurrencyEntity,Integer> {

    CurrencyEntity findCurrencyEntitiesByCode(CurrencyCode code);
}
