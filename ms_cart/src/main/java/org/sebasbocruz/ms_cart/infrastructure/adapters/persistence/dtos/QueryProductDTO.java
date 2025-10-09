package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos;

import lombok.Data;

@Data
public class QueryProductDTO {
    private Long product_id;
    private int quantity;
}
