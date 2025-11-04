package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.publics;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("public.country")
public class CountryEntity {

    @Id
    @Column("country_id")
    private Long countryId;

    @Column("name")
    private String name;

}
