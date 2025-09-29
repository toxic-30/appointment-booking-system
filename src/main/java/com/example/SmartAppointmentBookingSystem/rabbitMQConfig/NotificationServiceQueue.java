package com.example.SmartAppointmentBookingSystem.rabbitMQConfig;

import org.springframework.stereotype.Service;

import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;

@Service
public class NotificationServiceQueue {

    public void sendNotification(NotificationRequestDTO request) {
        // send email + sms (placeholder)
        System.out.println("📩 Sending email & SMS for Appointment "
                + request.getAppointmentId() + " at " + request.getScheduledAt());
    }
}