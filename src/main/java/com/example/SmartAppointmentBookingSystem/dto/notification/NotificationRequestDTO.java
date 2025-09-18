package com.example.SmartAppointmentBookingSystem.dto.notification;

import java.time.LocalDateTime;
import com.example.SmartAppointmentBookingSystem.enums.NotificationType;
import lombok.Data;

@Data
public class NotificationRequestDTO {
    private Long recipientId;
    private Long appointmentId; // optional
    private String message;
    private NotificationType type;
    private LocalDateTime scheduledAt; // optional (for reminders)
}
