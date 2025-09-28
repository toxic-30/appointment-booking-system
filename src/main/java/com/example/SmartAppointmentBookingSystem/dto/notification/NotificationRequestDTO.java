package com.example.SmartAppointmentBookingSystem.dto.notification;

import java.time.LocalDateTime;
import com.example.SmartAppointmentBookingSystem.enums.NotificationEvent;
import com.example.SmartAppointmentBookingSystem.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDTO {

    private Long recipientId;           // ID of the user who will receive the notification
    private Long appointmentId;         // optional, can link to an appointment
    private String message;             // message content
    private NotificationType type;      // type of notification (EMAIL, SMS, PUSH, etc.)
    private NotificationEvent event;
    private LocalDateTime scheduledAt;  // optional, for future reminders or delayed notifications

}