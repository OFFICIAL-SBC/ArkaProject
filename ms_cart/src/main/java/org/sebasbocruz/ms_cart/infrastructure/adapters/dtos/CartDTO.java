package org.sebasbocruz.ms_cart.infrastructure.adapters.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    final Double  totalPrice;
    final List<LineDTO> lines;
}
