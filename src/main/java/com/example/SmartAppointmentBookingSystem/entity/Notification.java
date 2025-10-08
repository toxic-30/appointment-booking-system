package com.example.SmartAppointmentBookingSystem.entity;

import java.time.LocalDateTime;
import com.example.SmartAppointmentBookingSystem.enums.NotificationStatus;
import com.example.SmartAppointmentBookingSystem.enums.NotificationType;
import com.example.SmartAppointmentBookingSystem.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User recipient;
    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    private String message; 
    @Enumerated(EnumType.STRING)
    private NotificationType type; 
    @Enumerated(EnumType.STRING)
    private NotificationStatus status; 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
    private LocalDateTime scheduledAt; 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
    private LocalDateTime sentAt;     
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata") 
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
      createdAt = TimeUtil.now();
      updatedAt = createdAt;} 
      @PreUpdate  
      protected void onUpdate() {
        updatedAt = TimeUtil.now();}

}
