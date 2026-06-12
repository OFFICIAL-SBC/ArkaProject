package org.sebasbocruz.ms_orders.infrastructure.adapters.repositories;

import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.publics.CityEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CityRepository extends R2dbcRepository<CityEntity, Long> {
}
