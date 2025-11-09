package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.messaging.consumer;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.DomainEvents.children.CartItemAdded;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.DomainEvents.children.CartItemQuantityChanged;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.DomainEvents.children.CartItemRemoved;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.DomainEvents.parents.CartItemEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {

    @RabbitListener(queues = "${inventory.rabbitmq.queue}")
    public void consumeInventoryEvents(CartItemEvent objectReceived) {

        switch (objectReceived) {
            case CartItemAdded event ->
                // Handle CartItemAdded event
                    System.out.println("CartItemAdded event received: " + event);
            case CartItemQuantityChanged event ->
                // Handle CartItemQuantityChanged event
                    System.out.println("CartItemQuantityChanged event received: " + event);
            case CartItemRemoved event ->
                // Handle CartItemRemoved event
                    System.out.println("CartItemRemoved event received: " + event);
            case null, default -> System.out.println("Unknown CartItemEvent type received: " + objectReceived);
        }
    }

}
