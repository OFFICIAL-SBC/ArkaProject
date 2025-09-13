package org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.query;

import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;

public interface StockPolicy { boolean isAvailable(ProductId productId, int quantity); }
