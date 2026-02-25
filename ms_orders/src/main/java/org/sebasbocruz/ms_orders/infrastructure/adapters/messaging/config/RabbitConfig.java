package org.sebasbocruz.ms_orders.infrastructure.adapters.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    // ? @Configuration -> Tells Spring that this class contains bean definitions and should be processed by the Spring container at startup

    public static final String EXCHANGE  = "order.event.exchange";
    public static final String ORDER_STATE_EVENT_QUEUE = "order.event.change.queue";


    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate (ConnectionFactory connectionFactory){

        // ? AmqpTemplate is an Interface for SENDING MESSAGES
        //? RabbitTemplate is the MOST COMMON IMPLEMENTATION OF THIS INTERFACE

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    // * Defining the only exchange for the order Scope
    @Bean
    public TopicExchange orderExchange(){
        return new TopicExchange(EXCHANGE,true,false);
    }

    //* Defining the queues
    @Bean
    Queue orderEventQueue(){
        return QueueBuilder.durable(ORDER_STATE_EVENT_QUEUE).build();
    }

    //* Defining the Binds
    Binding invCreated(TopicExchange orderExchange){
        return BindingBuilder.bind(orderEventQueue()).to(orderExchange).with("order.created");
    }

}
