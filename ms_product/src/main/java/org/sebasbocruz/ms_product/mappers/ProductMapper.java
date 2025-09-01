package org.sebasbocruz.ms_product.mappers;

import org.sebasbocruz.ms_product.dto.ProductDTO;
import org.sebasbocruz.ms_product.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public List<ProductDTO> mapFromEntityToDTO (List<Product> products){
        return products.stream().map(product -> {
            return ProductDTO.builder()
                    .name(product.getName())
                    .brand(product.getBrand().getName())
                    .price(product.getPrice())
                    .category(product.getCategory().getName())
                    .build();
        }).toList();
    }


}
