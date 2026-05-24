package org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects;

import java.math.BigDecimal;

public record Coordinates(
        BigDecimal latitude,
        BigDecimal longitude
) {

}
