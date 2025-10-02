package com.example.SmartAppointmentBookingSystem.config;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "notification.exchange";
    public static final String ROUTING_KEY = "notification.routingkey";
    public static final String QUEUE = "notification.queue";

    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct"); // underlying exchange type
        return new CustomExchange(EXCHANGE, "x-delayed-message", true, false, args);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding binding(Queue notificationQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(delayedExchange)
                .with(ROUTING_KEY)
                .noargs();
    }
}

