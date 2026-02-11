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
                // ! Side-effect only: logs errors but does NOT handle them
                .doOnError(e -> logger.error("Error processing event: {}", e.getMessage(), e))
                // ! Error recovery:
                // ! - Converts any failure into a successful empty completion
                // ! - Prevents listener crashes or endless retries
                .onErrorResume(e -> Mono.empty())
                // ! - Reactor chains are lazy
                // ! - Without subscribe(), nothing runs
                .subscribe();
    }


    private Mono<Void> processEvent(String json) {
        return Mono.fromCallable(() -> parseEvent(json))
                // ! flatMap is used because handleEvent returns Mono<Void>
                .flatMap(this::handleEvent)
                // ! Executed only if the entire pipeline completes successfully
                // ! For Mono<Void>, the value (v) is always null
                .doOnSuccess(v -> logger.debug("Event processed successfully"))
                .then();
    }

    /**
     * Parses the incoming message into a domain DTO.
     *
     * WHY throws Exception?
     * - Jackson parsing methods throw CHECKED exceptions
     * - Declaring throws defers error handling to the caller 💀💀💀💀💀
     * - In reactive code, thrown exceptions are converted to onError signals
     *
     * This is NOT an alternative to try/catch:
     * - It simply postpones error handling
     * - Reactor (Mono.fromCallable) will catch it for us
     *
     * NOTE:
     * The message contains a JSON string that itself wraps JSON:
     *   "{\"eventType\":\"cart.item.added\", ... }"
     * So we must unwrap it in two steps.
     */
    // ! throws Exception means -> I might fail, and I’m not handling the failure here. Whoever calls me must deal with it
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
