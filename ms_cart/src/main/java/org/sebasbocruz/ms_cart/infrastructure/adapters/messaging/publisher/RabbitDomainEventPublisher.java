package org.sebasbocruz.ms_cart.infrastructure.adapters.messaging.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.Parents.CartItemEvent;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainEvents.children.CartItemAdded;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.DomainEventPublisher;
import org.sebasbocruz.ms_cart.infrastructure.adapters.messaging.config.RabbitConfig;
import org.sebasbocruz.ms_cart.infrastructure.adapters.messaging.dto.InventoryEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitDomainEventPublisher implements DomainEventPublisher {

    private final Logger logger = LoggerFactory.getLogger(RabbitDomainEventPublisher.class);

    private final RabbitTemplate amqp;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(CartItemEvent event) {

        String rk = event.getClass().getSimpleName().replaceAll("([A-Z])", ".$1")
                .toLowerCase().substring(1); // cart.cart_opened

        InventoryEventDTO inventoryEventDTO = InventoryEventDTO.builder()
                .cartId(event.getCartId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .eventType(rk)
                .build();;

        try{

            String json = objectMapper.writeValueAsString(inventoryEventDTO);

            logger.warn("This is the Routing Key of the event {}", rk);
            logger.warn(inventoryEventDTO.toString());

            amqp.convertAndSend(RabbitConfig.EXCHANGE, rk, json);

        }catch (Exception e){
            logger.error("Error publishing event: {}", e.getMessage());
        }

    }
}