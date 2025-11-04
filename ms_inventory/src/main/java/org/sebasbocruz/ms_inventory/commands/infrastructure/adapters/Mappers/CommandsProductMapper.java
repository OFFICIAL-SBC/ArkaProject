package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.Mappers;

import org.sebasbocruz.ms_inventory.commands.domain.contexts.Product.Aggregate.Product;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.persistence.schemas.product.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class CommandsProductMapper {


    public ProductEntity fromClientToEntity(Long brandId, Long categoryId, String name, String description, double price, double discount) {
        return ProductEntity.builder()
                .name(name)
                .brandId(brandId)
                .categoryId(categoryId)
                .description(description)
                .price(price)
                .discount(discount)
                .build();
    }

    public Product fromEntityToDomain(ProductEntity productEntity) {
        return new Product(
                productEntity.getProductId(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getPrice(),
                productEntity.getDiscount()
        );
    }




}
