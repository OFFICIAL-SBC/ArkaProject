package org.sebasbocruz.ms_orders.domain.context.orders.entity;

import java.math.BigDecimal;

public record DeliveryAddress(
        String Line1,
        String City,
        String country,
        Coordinates coordinates
) {
}


record Coordinates(
        BigDecimal latitude,
        BigDecimal longitude
) {

}