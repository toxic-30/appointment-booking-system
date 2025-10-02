package com.example.SmartAppointmentBookingSystem.rabbitMQConfig;

import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import com.example.SmartAppointmentBookingSystem.config.RabbitMQConfig;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationServiceQueue {

    private final RabbitTemplate rabbitTemplate;

    public void scheduleNotification(NotificationRequestDTO reminderRequest) {
        LocalDateTime now = LocalDateTime.now();
        long delayMs = Duration.between(now, reminderRequest.getScheduledAt()).toMillis();

        if (delayMs <= 0) {
            // fallback if reminder time is in the past
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                reminderRequest
            );
            return;
        }

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.ROUTING_KEY,
            reminderRequest,
            message -> {
                message.getMessageProperties().setHeader("x-delay", delayMs);
                return message;
            }
        );

        System.out.println("Scheduled reminder for appointment " + reminderRequest.getAppointmentId()
            + " in " + delayMs/1000/60 + " minutes");
    }
}