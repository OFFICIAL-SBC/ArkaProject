package org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.ValueObjects;

public record Quantity(int value) {
    public Quantity {
        if (value < 0) throw new IllegalArgumentException("Quantity cannot be negative");
    }
    public Quantity add(int delta) { return new Quantity(value + delta); }
    public Quantity subtract(int delta) {
        if (value - delta < 0) throw new IllegalStateException("Insufficient quantity");
        return new Quantity(value - delta);
    }
}
