package org.sebasbocruz.ms_orders.infrastructure.adapters.repositories;

import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.publics.AddressEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface AddressRepository extends R2dbcRepository<AddressEntity, Long> {
}
