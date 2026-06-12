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
@Table(schema = "public",name = "country")
public class CountryEntity {

    @Id
    @Column("country_id")
    private Long countryId;

    @Column("name")
    private String countryName;

}
