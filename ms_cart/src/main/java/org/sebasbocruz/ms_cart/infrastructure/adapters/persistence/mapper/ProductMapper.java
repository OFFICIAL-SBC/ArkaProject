package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.mapper;

import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductName;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductPrice;
import org.sebasbocruz.ms_cart.domain.contexts.Product.entities.Product;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.posgressql.entity.schemas.product.ProductEntity;

public class ProductMapper {
    public static Product fromInfrastructureToDomain(ProductEntity productEntity){
        ProductId productId = new ProductId(productEntity.getId());
        ProductName productName = new ProductName(productEntity.getName());
        ProductPrice productPrice = new ProductPrice(productEntity.getPrice());
        return new Product(productId,productName,productPrice);
    }
}
