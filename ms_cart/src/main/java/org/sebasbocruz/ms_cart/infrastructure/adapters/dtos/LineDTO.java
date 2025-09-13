package org.sebasbocruz.ms_cart.infrastructure.adapters.dtos;

import lombok.Data;

@Data
public class LineDTO {
    private String productName;
    private int numberOfProducts;
    private Double subtotal;
}
