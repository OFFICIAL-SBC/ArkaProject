package org.sebasbocruz.ms_orders.domain.context.orders.ValueObjects;

import java.util.Objects;

public record Currency(String code) {
    public Currency{
        Objects.requireNonNull(code);
    }
}
