package com.example.SmartAppointmentBookingSystem.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendReminder(NotificationRequestDTO request) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                request
        );
        System.out.println("📤 Reminder published for appointmentId: " + request.getAppointmentId());
    }
}
