package com.example.SmartAppointmentBookingSystem.rabbitMQConfig;

import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import com.example.SmartAppointmentBookingSystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;
    @RabbitListener(queues = "notification.queue")
    public void handleNotificationQueue(NotificationRequestDTO request) {
        try {
            // Only send if the scheduled time is now or in the past
            if (request.getScheduledAt() == null || !request.getScheduledAt().isAfter(LocalDateTime.now())) {
                notificationService.sendNotification(request);
            } else {
                // If the scheduled time is in the future, leave it for scheduled task
                System.out.println("Notification scheduled for future: " + request.getScheduledAt());
            }
        } catch (Exception e) {
            System.err.println("Failed to process notification for appointment " + request.getAppointmentId());
            e.printStackTrace();
        }
    }
}
