package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class LineDTO {
    private Long productId;
    private String productName;
    private int numberOfProducts;
    private Double subtotal;
}
