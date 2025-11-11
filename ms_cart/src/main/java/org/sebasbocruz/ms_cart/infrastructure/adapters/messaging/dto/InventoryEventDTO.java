package org.sebasbocruz.ms_cart.infrastructure.adapters.messaging.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class InventoryEventDTO {
    private Long cartId;
    private Long productId;
    private int quantity;
    private String eventType;

}
