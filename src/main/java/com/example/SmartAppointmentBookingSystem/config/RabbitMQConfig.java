package com.example.SmartAppointmentBookingSystem.config;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "notification.exchange";
    public static final String ROUTING_KEY = "notification.routingkey";

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(EXCHANGE,true, false);
    }
    @Bean
    public Queue notificationQueue() {
        return new Queue("notification.queue", true, false, false);
    }
    @Bean
    public Binding binding(Queue notificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(notificationExchange)
                .with(ROUTING_KEY);
    }
}
