package org.sebasbocruz.ms_orders.infrastructure.adapters.repositories;

import org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Inventory.InventoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface InventoryRepository extends R2dbcRepository<InventoryEntity,Long> {

    @Query("""
        SELECT earth_distance(
            ll_to_earth(:lat1, :lon1),
            ll_to_earth(:lat2, :lon2)
        )
    """)
    Mono<Double> distanceBetween(
            @Param("lat1") double lat1,
            @Param("lon1") double lon1,
            @Param("lat2") double lat2,
            @Param("lon2") double lon2
    );

}
