package com.example.SmartAppointmentBookingSystem.dto.notification;

import java.time.LocalDateTime;
import com.example.SmartAppointmentBookingSystem.enums.NotificationStatus;
import com.example.SmartAppointmentBookingSystem.enums.NotificationType;
import lombok.Data;

@Data
public class NotificationResponseDTO {
    private Long id;
    private String recipientName;
    private String recipientEmail;
    private Long appointmentId;
    private String message;
    private NotificationType type;
    private NotificationStatus status;
    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}
