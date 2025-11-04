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
@Table("public.city")
public class CityEntity {

    @Id
    @Column("city_id")
    private Long cityId;

    @Column("name")
    private String name;
}
