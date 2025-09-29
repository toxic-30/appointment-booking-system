package com.example.SmartAppointmentBookingSystem.dto.notification;

import java.time.LocalDateTime;
import com.example.SmartAppointmentBookingSystem.enums.NotificationEvent;
import com.example.SmartAppointmentBookingSystem.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDTO {

    @NotNull
    private Long recipientId;           // ID of the user who will receive the notification

    private Long appointmentId;         // optional, can link to an appointment

    @NotBlank
    private String message;             // message content

    @NotNull
    private NotificationType type;      // type of notification (EMAIL, SMS, PUSH, etc.)

    private NotificationEvent event;

    @Future
    private LocalDateTime scheduledAt;  // optional, for future reminders or delayed notifications

}