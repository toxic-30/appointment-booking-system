package com.example.SmartAppointmentBookingSystem.rabbitMQConfig;

import com.example.SmartAppointmentBookingSystem.config.RabbitMQConfig;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import com.example.SmartAppointmentBookingSystem.service.NotificationService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;
    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleNotification(NotificationRequestDTO request) {
        try {
            log.info("Received notification for appointment " + request.getAppointmentId() + " at " + LocalDateTime.now());
            notificationService.sendNotification(request);
        } catch (Exception e) {
            log.error("Failed to schedule notification for appointmentId {}: {}", 
                      request.getAppointmentId(), e.getMessage(), e);
        }
    }
}