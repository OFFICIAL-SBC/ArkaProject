package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.DomainEvents.children.CartItemAdded;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.DomainEvents.children.CartItemQuantityChanged;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.DomainEvents.children.CartItemRemoved;
import org.sebasbocruz.ms_inventory.commands.domain.contexts.Inventory.DomainEvents.parents.CartItemEvent;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.messaging.dto.InventoryEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private Logger logger = LoggerFactory.getLogger(InventoryEventConsumer.class);

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${inventory.rabbitmq.queue}")
    public void consumeInventoryEvents(String json) {

        try {
            // Unwrap nested JSON string first
            String innerJson = objectMapper.readValue(json, String.class);

            // Then deserialize the actual event
            InventoryEventDTO event = objectMapper.readValue(innerJson, InventoryEventDTO.class);

            System.out.println("✅ Received and deserialized event: " + event);

            // TODO: call your use case or business logic here

        } catch (Exception e) {
            logger.error("Error processing event: {}", e.getMessage());
        }

    }

}
