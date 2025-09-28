package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import com.example.SmartAppointmentBookingSystem.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    // Listen to the queue for scheduled reminders
    @RabbitListener(queues = "notification.queue")
    public void handleReminder(NotificationRequestDTO request) {
        try {
            // Send notification
            notificationService.sendNotification(request);

            System.out.println(" Reminder sent for appointmentId: " + request.getAppointmentId());
        } catch (Exception e) {
            System.err.println(" Failed to send reminder for appointmentId: " + request.getAppointmentId());
            e.printStackTrace();
        }
    }
}
