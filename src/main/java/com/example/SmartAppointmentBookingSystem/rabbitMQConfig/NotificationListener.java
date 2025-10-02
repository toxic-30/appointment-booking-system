package com.example.SmartAppointmentBookingSystem.rabbitMQConfig;

import com.example.SmartAppointmentBookingSystem.config.RabbitMQConfig;
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
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleNotification(NotificationRequestDTO request) {
        try {
            notificationService.sendNotification(request);
            System.out.println("Reminder sent for appointment " + request.getAppointmentId()
                + " at " + LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
