package org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Warehouse {
    private Long id;
    private String name;
    private String address;
    private String Country;
    private String City;
}
