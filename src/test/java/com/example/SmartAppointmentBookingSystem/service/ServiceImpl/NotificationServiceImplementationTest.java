package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.Appointment;
import com.example.SmartAppointmentBookingSystem.entity.Notification;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.NotificationEvent;
import com.example.SmartAppointmentBookingSystem.enums.NotificationStatus;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.rabbitMQConfig.NotificationServiceQueue;
import com.example.SmartAppointmentBookingSystem.repository.AppointmentRepository;
import com.example.SmartAppointmentBookingSystem.repository.NotificationRepository;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class NotificationServiceImplementationTest {

    @Mock NotificationRepository notificationRepo;
    @Mock UserRepository userRepo;
    @Mock AppointmentRepository appointmentRepo;
    @Mock NotificationServiceQueue emailService;
    @Mock RabbitTemplate rabbitTemplate;

    @InjectMocks NotificationServiceImplementation service;

    @Test
    void getNotificationById_found() {
        Notification notification = Notification.builder().id(1L).recipient(new User()).build();
        when(notificationRepo.findById(1L)).thenReturn(Optional.of(notification));
        NotificationResponseDTO dto = service.getNotificationById(1L);
        assertEquals(1L, dto.getId());
    }

    @Test
    void getNotificationById_notFound() {
        when(notificationRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getNotificationById(1L));
    }

    @Test
    void createNotification_withAppointment() {
        NotificationRequestDTO req = new NotificationRequestDTO();
        req.setRecipientId(2L);
        req.setAppointmentId(3L);
        req.setMessage("msg");
        req.setEvent(NotificationEvent.APPOINTMENT_REMINDER);
        req.setScheduledAt(LocalDateTime.now());

        User user = new User();
        Appointment appt = new Appointment();
        when(userRepo.findById(2L)).thenReturn(Optional.of(user));
        when(appointmentRepo.findById(3L)).thenReturn(Optional.of(appt));
        when(notificationRepo.save(any())).thenAnswer(i -> i.getArgument(0));

    NotificationResponseDTO dto = service.createNotification(req, NotificationStatus.SENT);
    assertEquals("msg", dto.getMessage());
    assertEquals(NotificationStatus.SENT, dto.getStatus());
    // the appointment object in test has no id set, so appointmentId in response should be null
    assertNull(dto.getAppointmentId());
    }

    @Test
    void updateStatus_valid() {
        User recipient = new User(); recipient.setId(2L); recipient.setName("User"); recipient.setEmail("u@e.com");
        Notification notification = Notification.builder().id(1L).status(NotificationStatus.PENDING).recipient(recipient).build();
        when(notificationRepo.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        NotificationResponseDTO dto = service.updateStatus(1L, "SENT");
        assertEquals(NotificationStatus.SENT, dto.getStatus());
    }

    @Test
    void updateStatus_invalid() {
        Notification notification = Notification.builder().id(1L).status(NotificationStatus.PENDING).build();
        when(notificationRepo.findById(1L)).thenReturn(Optional.of(notification));
        assertThrows(ResourceNotFoundException.class, () -> service.updateStatus(1L, "INVALID"));
    }

    @Test
    void sendNotification_callsEmailService() {
        NotificationRequestDTO req = new NotificationRequestDTO();
        req.setRecipientId(2L);
        User user = new User();
        when(userRepo.findById(2L)).thenReturn(Optional.of(user));
        when(notificationRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        doNothing().when(emailService).processNotification(req);
        service.sendNotification(req);
        verify(emailService, times(1)).processNotification(req);
    }

    @Test
    void scheduleNotification_callsRabbitTemplate() {
        NotificationRequestDTO req = new NotificationRequestDTO();
        req.setRecipientId(2L);
        User user = new User();
        when(userRepo.findById(2L)).thenReturn(Optional.of(user));
        when(notificationRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), eq(req));
        service.scheduleNotification(req);
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), eq(req));
    }
}
