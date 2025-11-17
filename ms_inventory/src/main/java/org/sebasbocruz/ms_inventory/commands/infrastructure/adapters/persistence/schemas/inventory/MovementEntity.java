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
@Table("inventory.movement")
public class MovementEntity {

    @Id
    @Column("movement_id")
    private Long movementId;

    @Column("inventory_id")
    private Long inventoryId;

    @Column("movement_type")
    private String movementType;

    @Column("reference_type")
    private String referenceType;

    @Column("quantity")
    private Integer quantity;

    @CreatedDate
    @Column("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private Instant updatedAt;

}
