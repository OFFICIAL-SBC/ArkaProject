package org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.sebasbocruz.ms_inventory.commands.domain.commons.movement.MovementType;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.ValueObjects.Quantity;

import java.time.Instant;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class Inventory {
    private final Long id;
    private final Long productId;
    private final Long warehouseId; // or WarehouseId VO if you model it here
    private Quantity available;
    private Quantity threshold;
    private final Instant createdAt;
    private Instant updatedAt;

    public Inventory(Long id, Long productId, Long warehouseId,
                     Quantity available, Quantity threshold) {
        this.id = Objects.requireNonNull(id);
        this.productId = Objects.requireNonNull(productId);
        this.warehouseId = Objects.requireNonNull(warehouseId);
        this.available = Objects.requireNonNull(available);
        this.threshold = Objects.requireNonNull(threshold);
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    public void applyMovement(MovementType type, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be > 0");
        this.available = (type == MovementType.ADD)
                ? available.add(amount)
                : available.subtract(amount);
    }

    public boolean isBelowThreshold() { return available.value() < threshold.value(); }

}
