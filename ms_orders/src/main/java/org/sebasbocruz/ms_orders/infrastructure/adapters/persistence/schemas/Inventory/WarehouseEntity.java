package org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.Inventory;

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
@Table(schema = "inventory",name = "warehouse")
public class WarehouseEntity {

    @Id
    @Column("warehouse_id")
    private Long warehouseId;

    @Column("address_id")
    private Long addressId;

    @Column("name")
    private String name;

}
