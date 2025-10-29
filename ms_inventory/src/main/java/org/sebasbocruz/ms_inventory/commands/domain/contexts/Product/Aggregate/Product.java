package org.sebasbocruz.ms_inventory.commands.domain.contexts.Product.Aggregate;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class Product {
    private final Long id;
    private String name;
    private String description;
    private double price;
    private double discount; // 0..1 or absolute — choose one and document
    private Instant createdAt;
    private Instant updatedAt;

    public Product(Long id, String name, String description, double price, double discount) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.price = Objects.requireNonNull(price);
        this.discount = discount;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

}
