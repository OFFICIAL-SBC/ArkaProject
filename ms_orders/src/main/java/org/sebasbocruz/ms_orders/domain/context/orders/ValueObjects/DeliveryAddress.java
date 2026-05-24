package org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects;

import java.math.BigDecimal;

public record DeliveryAddress(
        String Line1,
        String City,
        String country,
        Coordinates coordinates
) {
}


