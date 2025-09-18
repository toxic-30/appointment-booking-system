package com.example.SmartAppointmentBookingSystem.entity;

import java.time.LocalDateTime;
import com.example.SmartAppointmentBookingSystem.enums.NotificationStatus;
import com.example.SmartAppointmentBookingSystem.enums.NotificationType;
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
    private LocalDateTime scheduledAt; 
    private LocalDateTime sentAt;      
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    } 
    @PreUpdate  
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
