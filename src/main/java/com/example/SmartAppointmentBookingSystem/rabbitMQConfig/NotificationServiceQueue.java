package com.example.SmartAppointmentBookingSystem.rabbitMQConfig;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import com.example.SmartAppointmentBookingSystem.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationServiceQueue {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceQueue.class);

    private final NotificationService notificationService;

    // Listen to the queue where reminders are published
    @RabbitListener(queues = "notification.queue")
    public void processNotification(NotificationRequestDTO request) {
        logger.info("📩 Consuming from queue: Appointment {} scheduled at {}",
                request.getAppointmentId(), request.getScheduledAt());

        try {
            // Only send notifications that are scheduled for now or in the past
            if (request.getScheduledAt() == null || !request.getScheduledAt().isAfter(LocalDateTime.now())) {
                notificationService.sendNotification(request);
                logger.info("Notification sent for Appointment {}", request.getAppointmentId());
            } else {
                logger.info("⏳ Notification scheduled for future: {} (Appointment {})",
                        request.getScheduledAt(), request.getAppointmentId());
            }
        } catch (Exception e) {
            logger.error("Failed to process notification for Appointment {}: {}",
                    request.getAppointmentId(), e.getMessage(), e);
        }
    }
}
