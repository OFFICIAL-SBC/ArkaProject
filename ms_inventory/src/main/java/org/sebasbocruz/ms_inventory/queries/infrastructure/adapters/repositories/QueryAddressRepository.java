package org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.repositories;

import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory.AddressEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface QueryAddressRepository extends R2dbcRepository<AddressEntity,Long> {
}
