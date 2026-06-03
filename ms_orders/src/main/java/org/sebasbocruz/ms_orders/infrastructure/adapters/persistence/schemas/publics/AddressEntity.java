package org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.publics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(schema = "public",name = "address")
public class AddressEntity {

    @Id
    @Column("address_id")
    private Long addressId;

    @Column("city_id")
    private Long cityId;

    @Column("country_id")
    private Long countryId;

    @Column("address")
    private String address;

    @Column("lat")
    private double latitude;

    @Column("lon")
    private double longitude;

}
