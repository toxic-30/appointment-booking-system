package com.example.SmartAppointmentBookingSystem.rabbitMQConfig;

import java.time.Duration;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import com.example.SmartAppointmentBookingSystem.config.RabbitMQConfig;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationServiceQueue {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceQueue.class);
    private final RabbitTemplate rabbitTemplate;

    public void scheduleNotification(NotificationRequestDTO reminderRequest) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduledAt = reminderRequest.getScheduledAt();
        if (scheduledAt == null) {
            scheduledAt = now; // fallback to send immediately
        }
        long delayMs = Math.max(0, Duration.between(now, scheduledAt).toMillis());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                reminderRequest,
                message -> {
                    message.getMessageProperties().setHeader("x-delay", delayMs);
                    return message;
                }
        );
        log.info("Scheduled notification for appointment "+ reminderRequest.getAppointmentId()+ " in " + delayMs / 1000 / 60 + " minutes");
    }
}