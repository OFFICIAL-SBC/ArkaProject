package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.inventory;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("inventory.warehouse")
public class WarehouseEntity {

    @Id
    @Column("warehouse_id")
    private Long warehouseId;

    @Column("address_id")
    private Long addressId;

    @Column("client_id")
    private Long clientId;

    @Column("provider_id")
    private Long providerId;

    @Column("ownership")
    private Boolean ownership;

    @Column("name")
    private String name;

    @CreatedDate
    @Column("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private Instant updatedAt;
}
