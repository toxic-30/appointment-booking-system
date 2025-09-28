package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.Appointment;
import com.example.SmartAppointmentBookingSystem.entity.Notification;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.NotificationStatus;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.AppointmentRepository;
import com.example.SmartAppointmentBookingSystem.repository.NotificationRepository;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;
import com.example.SmartAppointmentBookingSystem.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImplementation implements NotificationService {

    private final NotificationRepository notificationRepo;
    private final UserRepository userRepo;
    private final AppointmentRepository appointmentRepo;
    private final EmailService emailService;        // sends email
    private final RabbitTemplate rabbitTemplate;    // RabbitMQ template
    
        @Override
    public NotificationResponseDTO getNotificationById(Long id) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        return toResponseDTO(notification);
    }

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO request, NotificationStatus status) {
        User recipient = userRepo.findById(request.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getRecipientId()));
        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepo.findById(request.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + request.getAppointmentId()));
        }
        Notification notification = Notification.builder()
                .recipient(recipient)
                .appointment(appointment)
                .message(request.getMessage())
                .type(request.getType())
                .status(status)
                .scheduledAt(request.getScheduledAt())
                .sentAt(status == NotificationStatus.SENT ? LocalDateTime.now() : null)
                .build();

        return toResponseDTO(notificationRepo.save(notification));
    }

    @Override
    public NotificationResponseDTO updateStatus(Long id, String status) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        try {
            notification.setStatus(NotificationStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid status: " + status);
        }
        Notification updated = notificationRepo.save(notification);
        return toResponseDTO(updated);
    }

    @Override
    public void sendNotification(NotificationRequestDTO request) {
        NotificationResponseDTO notification = createNotification(request, NotificationStatus.SENT);
        // Send email (can integrate SMS / Push similarly)
        emailService.sendEmail(
                notification.getRecipientEmail(),
                "Appointment Notification",
                notification.getMessage()
        );
        // Optionally log message sent
        System.out.println("Notification sent: " + notification.getMessage());
    }

    @Override
    public void scheduleNotification(NotificationRequestDTO request) {
        createNotification(request, NotificationStatus.PENDING);
        // Send to RabbitMQ queue for async processing
        rabbitTemplate.convertAndSend("notification.exchange", "notification.routingkey", request);
    }

    private NotificationResponseDTO toResponseDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setRecipientName(notification.getRecipient().getName());
        dto.setRecipientEmail(notification.getRecipient().getEmail());
        dto.setAppointmentId(notification.getAppointment() != null ? notification.getAppointment().getId() : null);
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setStatus(notification.getStatus());
        dto.setScheduledAt(notification.getScheduledAt());
        dto.setSentAt(notification.getSentAt());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
    
}
