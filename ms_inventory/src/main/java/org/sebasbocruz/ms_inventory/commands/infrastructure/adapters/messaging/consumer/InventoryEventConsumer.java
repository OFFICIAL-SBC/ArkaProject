package org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_inventory.commands.application.CartItemAddedToCartUseCase;
import org.sebasbocruz.ms_inventory.commands.application.CartQuantityItemChangedUseCase;
import org.sebasbocruz.ms_inventory.commands.application.RemoveItemFromCartUseCase;
import org.sebasbocruz.ms_inventory.commands.infrastructure.adapters.messaging.dto.InventoryEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {

    //TODO: I need to understand this code.

    private static final Logger logger = LoggerFactory.getLogger(InventoryEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final CartItemAddedToCartUseCase cartItemAddedToCartUseCase;
    private final RemoveItemFromCartUseCase removeItemFromCartUseCase;
    private final CartQuantityItemChangedUseCase cartQuantityItemChangedUseCase;

    @RabbitListener(queues = "${inventory.rabbitmq.queue}")
    public void consumeInventoryEvents(String json) {
        processEvent(json)
                .doOnError(e -> logger.error("Error processing event: {}", e.getMessage(), e))
                .onErrorResume(e -> Mono.empty()) // Prevent listener from crashing
                .subscribe(); // Subscribe to trigger execution
    }

    private Mono<Void> processEvent(String json) {
        return Mono.fromCallable(() -> parseEvent(json))
                .flatMap(this::handleEvent)
                .doOnSuccess(v -> logger.debug("Event processed successfully"))
                .then();
    }

    private InventoryEventDTO parseEvent(String json) throws Exception {
        // Unwrap nested JSON string first
        String innerJson = objectMapper.readValue(json, String.class);
        // Then deserialize the actual event
        InventoryEventDTO event = objectMapper.readValue(innerJson, InventoryEventDTO.class);
        logger.info("Received event: {}", event);
        return event;
    }

    private Mono<Void> handleEvent(InventoryEventDTO event) {
        return switch (event.getEventType()) {
            case "cart.item.added" ->
                    cartItemAddedToCartUseCase.execute(
                            event.getCartId(),
                            event.getProductId(),
                            event.getQuantity()
                    );
            case "cart.item.quantity.changed" ->
                    cartQuantityItemChangedUseCase.execute(
                            event.getCartId(),
                            event.getProductId(),
                            event.getQuantity()
                    );
            case "cart.item.removed" ->
                    removeItemFromCartUseCase.execute(
                            event.getCartId(),
                            event.getProductId(),
                            event.getQuantity()
                    );
            default -> {
                logger.warn("Unknown event type: {}", event.getEventType());
                yield Mono.empty();
            }
        };
    }
}
