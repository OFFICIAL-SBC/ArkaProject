package org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos;

import lombok.Data;
import org.sebasbocruz.ms_cart.domain.commons.enums.CurrencyCode;

import java.util.List;

@Data
public class CartDTO {
    final CurrencyCode currencyCode;
    final Double  totalPrice;
    final List<LineDTO> lines;
    final Long userID;
}
