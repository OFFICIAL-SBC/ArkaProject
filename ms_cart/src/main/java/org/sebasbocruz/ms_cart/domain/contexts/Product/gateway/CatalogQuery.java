package org.sebasbocruz.ms_cart.domain.contexts.Product.gateway;

import org.sebasbocruz.ms_cart.domain.contexts.Product.entities.Product;

import java.util.Optional;

public interface CatalogQuery {
    Optional<Product> getProduct(String productName);
}
