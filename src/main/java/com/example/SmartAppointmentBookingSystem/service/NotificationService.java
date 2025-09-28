package com.example.SmartAppointmentBookingSystem.service;

import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationResponseDTO;
import com.example.SmartAppointmentBookingSystem.enums.NotificationStatus;

public interface NotificationService {

    NotificationResponseDTO getNotificationById(Long id);
    NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO, NotificationStatus status);
    NotificationResponseDTO updateStatus(Long id, String status);
    void sendNotification(NotificationRequestDTO request);   // Immediate notification
    void scheduleNotification(NotificationRequestDTO request); // For reminders via queue
    
}
