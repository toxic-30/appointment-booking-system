package com.example.SmartAppointmentBookingSystem.entity;

import java.time.LocalDateTime;
import com.example.SmartAppointmentBookingSystem.enums.NotificationStatus;
import com.example.SmartAppointmentBookingSystem.enums.NotificationType;
import jakarta.persistence.*;

@Entity
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

    public Notification(Long id, User recipient, Appointment appointment, String message, NotificationType type,
            NotificationStatus status, LocalDateTime scheduledAt, LocalDateTime sentAt, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.recipient = recipient;
        this.appointment = appointment;
        this.message = message;
        this.type = type;
        this.status = status;
        this.scheduledAt = scheduledAt;
        this.sentAt = sentAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Notification() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getRecipient() {
        return recipient;
    }
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
    public Appointment getAppointment() {
        return appointment;
    }
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public NotificationType getType() {
        return type;
    }
    public void setType(NotificationType type) {
        this.type = type;
    }
    public NotificationStatus getStatus() {
        return status;
    }
    public void setStatus(NotificationStatus status) {
        this.status = status;
    }
    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }
    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
    public LocalDateTime getSentAt() {
        return sentAt;
    }
    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    @Override
    public String toString() {
        return "Notification [id=" + id + ", recipient=" + recipient + ", appointment=" + appointment + ", message="
                + message + ", type=" + type + ", status=" + status + ", scheduledAt=" + scheduledAt + ", sentAt="
                + sentAt + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
