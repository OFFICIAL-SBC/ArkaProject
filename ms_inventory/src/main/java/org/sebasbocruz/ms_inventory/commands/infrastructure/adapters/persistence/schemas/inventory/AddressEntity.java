package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("public.address")
public class AddressEntity {
    @Id
    @Column("address_id")
    private Long addressId;

    @Column("address")
    private String address;
}
