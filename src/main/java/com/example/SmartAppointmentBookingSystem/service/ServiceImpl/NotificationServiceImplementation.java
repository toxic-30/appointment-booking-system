package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.Appointment;
import com.example.SmartAppointmentBookingSystem.entity.Notification;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.NotificationStatus;
import com.example.SmartAppointmentBookingSystem.repository.AppointmentRepository;
import com.example.SmartAppointmentBookingSystem.repository.NotificationRepository;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;
import com.example.SmartAppointmentBookingSystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImplementation implements NotificationService {

    private final NotificationRepository notificationRepo;
    private final UserRepository userRepo;
    private final AppointmentRepository appointmentRepo;
    
    @Override
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationRepo.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    @Override
    public NotificationResponseDTO getNotificationById(Long id) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        return toResponseDTO(notification);
    }
    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO) {
        User recipient = userRepo.findById(requestDTO.getRecipientId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + requestDTO.getRecipientId()));
        Appointment appointment = null;
        if (requestDTO.getAppointmentId() != null) {
            appointment = appointmentRepo.findById(requestDTO.getAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + requestDTO.getAppointmentId()));
        }
        Notification notification = Notification.builder()
                .recipient(recipient)
                .appointment(appointment)
                .message(requestDTO.getMessage())
                .type(requestDTO.getType())
                .status(NotificationStatus.PENDING)
                .scheduledAt(requestDTO.getScheduledAt())
                .build();
        Notification saved = notificationRepo.save(notification);
        return toResponseDTO(saved);
    }

    @Override
    public NotificationResponseDTO updateStatus(Long id, String status) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        try {
            notification.setStatus(NotificationStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }
        Notification updated = notificationRepo.save(notification);
        return toResponseDTO(updated);
    }

    @Override
    public void deleteNotification(Long id) {
        if (!notificationRepo.existsById(id)) {
            throw new RuntimeException("Notification not found with id: " + id);
        }
        notificationRepo.deleteById(id);
    }
    
    private NotificationResponseDTO toResponseDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setRecipientName(notification.getRecipient() != null ? notification.getRecipient().getName() : null);
        dto.setRecipientEmail(notification.getRecipient() != null ? notification.getRecipient().getEmail() : null);
        if (notification.getAppointment() != null) {
            dto.setAppointmentId(notification.getAppointment().getId());
        }
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setStatus(notification.getStatus());
        dto.setScheduledAt(notification.getScheduledAt());
        dto.setSentAt(notification.getSentAt());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }

    
}
