package org.sebasbocruz.ms_cart.domain.contexts.Product.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductName;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductPrice;


@AllArgsConstructor
@Getter
@Setter
public class Product {
    private final ProductId id;
    private ProductName name;
    private ProductPrice price;
}
