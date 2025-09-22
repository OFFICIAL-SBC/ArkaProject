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
    public static final String QUEUE = "cart.event.queue";
    // sugerencia de routing keys: cart.opened, cart.item.added, cart.converted...

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


    @Bean
    public Queue cartQueue(){
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange cartExchange() { return new TopicExchange(EXCHANGE, true, false); }

    // ! Binding a queue and exchange using Routing Key
    @Bean
    public Binding cartBinding() {
        return BindingBuilder.bind(cartQueue())
                .to(cartExchange())
                .with("cart.event.routing.key");
    }

    // * SpringBoot will automatically configure these 3 beans by default
    // ConnectionFactory
    // RabbitTemplate
    // RabbitAdmin
}