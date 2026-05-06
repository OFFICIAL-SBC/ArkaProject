package org.sebasbocruz.ms_orders.domain.commons.states.Cart;

import java.util.Map;

public abstract class CartState {
    public static Map<Integer, String> CART_STATES = Map.of(
            1,"OPEN",
            2,"INACTIVE",
            3,"ABANDONED",
            4,"CANCELLED",
            5,"CONVERTED"
    );
}
