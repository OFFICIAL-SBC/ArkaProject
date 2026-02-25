package org.sebasbocruz.ms_cart.infrastructure.adapters.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "cart.event.exchange";


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate (ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }


    // * Defining the only exchange
    @Bean
    public TopicExchange cartExchange() { return new TopicExchange(EXCHANGE, true, false); }


    // * Defining the queues
    @Bean
    Queue inventoryQueue(){
        return QueueBuilder.durable("cart.inventory.queue").build();
    }

    @Bean
    Queue cartSateChangeQueue(){
        return QueueBuilder.durable("cart.state.change.queue").build();
    }

    // * Defining the Bindings
    // Inventory
    @Bean
    Binding invItems(TopicExchange cartExchange) {
        return BindingBuilder.bind(inventoryQueue()).to(cartExchange).with("cart.item.#");
    }
    @Bean
    Binding invConverted(TopicExchange cartExchange) {
        return BindingBuilder.bind(cartSateChangeQueue()).to(cartExchange).with("cart.converted");
    }
    @Bean
    Binding invCancelled(TopicExchange cartExchange) {
        return BindingBuilder.bind(cartSateChangeQueue()).to(cartExchange).with("cart.cancelled");
    }
    @Bean
    Binding invEmptied(TopicExchange cartExchange) {
        return BindingBuilder.bind(cartSateChangeQueue()).to(cartExchange).with("cart.emptied");
    }

    // Billing
    @Bean
    Binding billConverted(TopicExchange cartExchange) {
        return BindingBuilder.bind(cartSateChangeQueue()).to(cartExchange).with("cart.converted");
    }

    // Notifications
    @Bean
    Binding notifLifecycle(TopicExchange cartExchange) {
        return BindingBuilder.bind(cartSateChangeQueue()).to(cartExchange)
                .with("cart.abandoned");
    }


    // ! SpringBoot will automatically configure these 3 beans by default
    // ! ConnectionFactory
    // ! RabbitTemplate
    // ! RabbitAdmin
}