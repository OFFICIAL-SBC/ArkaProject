package org.sebasbocruz.ms_orders.infrastructure.adapters.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_orders.infrastructure.adapters.messaging.dto.CartStateEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartStateChangeEventConsumer {

    private static Logger logger = LoggerFactory.getLogger(CartStateChangeEventConsumer.class);
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "cart.state.change.queue")
    public void consumeCartStateChangeEvent(String json) {
        // Implementation to consume cart state change events
        processEvent(json)

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

    private Mono<Void> processEvent(String json){
        return Mono.fromCallable(() -> parseEvent(json))
                .flatMap(cartStateEventDTO -> {
                    return handleEvent(cartStateEventDTO);
                }).then();

    }

    private CartStateEventDTO parseEvent(String json) throws Exception{

        //Unwrap nested JSON string first
        String innerJson = objectMapper.readValue(json, String.class);

        //Then deserialize the actual event
        CartStateEventDTO event = objectMapper.readValue(innerJson, CartStateEventDTO.class);
        logger.warn("Receive event {}",event);
        return event;
    }

    private Mono<Void> handleEvent(CartStateEventDTO event){
        return switch(event.getState()){
            case "CONVERTED" ->
                Mono.empty();
            default ->
                Mono.empty();

        };
    }

}
