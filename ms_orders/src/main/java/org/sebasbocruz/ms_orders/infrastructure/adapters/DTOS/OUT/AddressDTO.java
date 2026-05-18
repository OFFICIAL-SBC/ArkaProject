package org.sebasbocruz.ms_orders.infrastructure.adapters.DTOS.OUT;

public record AddressDTO(
        String line1,
        String City,
        String country
) { }
