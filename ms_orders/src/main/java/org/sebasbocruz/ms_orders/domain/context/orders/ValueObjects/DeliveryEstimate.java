package org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects;

import java.time.OffsetDateTime;

public record DeliveryEstimate (
        OffsetDateTime earlist,
        OffsetDateTime latest
) {
}
