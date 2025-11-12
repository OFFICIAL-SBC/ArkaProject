package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEventDTO {
    private Long cartId;
    private Long productId;
    private int quantity;
    private String eventType;
}
