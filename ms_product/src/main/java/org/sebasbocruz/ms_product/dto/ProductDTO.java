package org.sebasbocruz.ms_product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {
    private String name;
    private double price;
    private String brand;
    private String category;
}
