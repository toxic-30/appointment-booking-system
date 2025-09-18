package com.example.SmartAppointmentBookingSystem.service;

import java.util.List;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationResponseDTO;

public interface NotificationService {

    List<NotificationResponseDTO> getAllNotifications();
    NotificationResponseDTO getNotificationById(Long id);
    NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO);
    NotificationResponseDTO updateStatus(Long id, String status);
    void deleteNotification(Long id);
    
}
