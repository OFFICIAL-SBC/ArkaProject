package org.sebasbocruz.ms_orders.infrastructure.adapters.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_orders.infrastructure.adapters.messaging.dto.CartStateEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartStateChangeEventConsumer {

    private Logger logger = LoggerFactory.getLogger(CartStateChangeEventConsumer.class);

    private final ObjectMapper objectMapper;

    public void consumeCartStateChangeEvent(String json) {
        // Implementation to consume cart state change events

        try{
            String innerJson = objectMapper.readValue(json, String.class);
            CartStateEventDTO event = objectMapper.readValue(innerJson, CartStateEventDTO.class);

            if(Objects.equals(event.getState(), "CONVERTED")){
                logger.info("Cart with ID {} has been converted to an order.", event.getCartId());
                // Additional logic to handle the cart conversion can be added here
            }

        }catch (Exception e) {
            logger.error("Error processing event: {}", e.getMessage());
        }
    }

}
