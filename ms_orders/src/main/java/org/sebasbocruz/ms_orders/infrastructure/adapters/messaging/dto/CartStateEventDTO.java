package org.sebasbocruz.ms_orders.infrastructure.adapters.messaging.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartStateEventDTO {
    private Long cartId;
    private String state;
}
