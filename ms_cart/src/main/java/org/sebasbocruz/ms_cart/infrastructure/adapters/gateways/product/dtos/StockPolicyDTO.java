package org.sebasbocruz.ms_cart.infrastructure.adapters.gateways.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class StockPolicyDTO {
    private Integer availableStock;
}
