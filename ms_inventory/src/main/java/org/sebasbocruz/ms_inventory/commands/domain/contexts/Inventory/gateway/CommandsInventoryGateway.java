package org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.gateway;


import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Aggregate.Inventory;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.Entity.Warehouse;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Product.Aggregate.Product;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.dtos.InventoryDTOcommands;
import reactor.core.publisher.Mono;

public interface CommandsInventoryGateway {

    Mono<Inventory> registerNewProduct(InventoryDTOcommands inventoryDTOcommands);
    Mono<Warehouse> doesWarehouseExist(Long warehouseId);
    Mono<Boolean> doesProductExistByName(String name);
    Mono<Boolean> doesProductExistById(Long product_id);
    Mono<Boolean> doesBrandExist(Long brandId);
    Mono<Boolean> doesCategoryExist(Long categoryId);

    Mono<Void> handleCartItemAddedToCart(Long cartId, Long productId, int quantity);

    Mono<Void> handleRemoveItemFromCart(Long cartId, Long productId, int quantity);

    Mono<Void> handleCartQuantityItemChanged(Long cartId, Long productId, int quantity);
}
