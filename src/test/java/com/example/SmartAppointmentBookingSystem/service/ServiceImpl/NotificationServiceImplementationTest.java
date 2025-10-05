package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.Appointment;
import com.example.SmartAppointmentBookingSystem.entity.Notification;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.NotificationStatus;
import com.example.SmartAppointmentBookingSystem.enums.NotificationType;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.AppointmentRepository;
import com.example.SmartAppointmentBookingSystem.repository.NotificationRepository;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;
import com.example.SmartAppointmentBookingSystem.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class NotificationServiceImplementationTest {

    @Mock private NotificationRepository notificationRepo;
    @Mock private UserRepository userRepo;
    @Mock private AppointmentRepository appointmentRepo;
    @Mock private EmailService emailService;
    @Mock private RabbitTemplate rabbitTemplate;

    @InjectMocks private NotificationServiceImplementation notificationService;

    private User user;
    private Appointment appointment;
    private NotificationRequestDTO request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        appointment = new Appointment();
        appointment.setId(2L);
        appointment.setAppointmentTime(LocalDateTime.now().plusDays(2));
        request = new NotificationRequestDTO();
        request.setRecipientId(1L);
        request.setAppointmentId(2L);
        request.setMessage("Reminder");
        request.setType(NotificationType.EMAIL);
    }

    @Test
    void testGetNotificationById_Success() {
        Notification notification = Notification.builder().id(1L).recipient(user).message("Hello").status(NotificationStatus.SENT).build();
        when(notificationRepo.findById(1L)).thenReturn(Optional.of(notification));

        NotificationResponseDTO response = notificationService.getNotificationById(1L);
        assertEquals("Hello", response.getMessage());
        assertEquals("John Doe", response.getRecipientName());
    }

    @Test
    void testGetNotificationById_NotFound() {
        when(notificationRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> notificationService.getNotificationById(1L));
    }

    @Test
    void testCreateNotification_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(appointmentRepo.findById(2L)).thenReturn(Optional.of(appointment));
        when(notificationRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        NotificationResponseDTO response = notificationService.createNotification(request, NotificationStatus.PENDING);
        assertEquals("Reminder", response.getMessage());
        assertEquals(NotificationStatus.PENDING, response.getStatus());
    }

    @Test
    void testUpdateStatus_Success() {
        Notification notification = Notification.builder().id(1L).status(NotificationStatus.PENDING).build();
        when(notificationRepo.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        NotificationResponseDTO response = notificationService.updateStatus(1L, "SENT");
        assertEquals(NotificationStatus.SENT, response.getStatus());
    }

    @Test
    void testUpdateStatus_Invalid() {
        Notification notification = Notification.builder().id(1L).status(NotificationStatus.PENDING).build();
        when(notificationRepo.findById(1L)).thenReturn(Optional.of(notification));

        assertThrows(ResourceNotFoundException.class, () -> notificationService.updateStatus(1L, "INVALID"));
    }

    @Test
    void testSendNotification_Success() {
        // Arrange
    when(userRepo.findById(1L)).thenReturn(Optional.of(user));
    when(appointmentRepo.findById(2L)).thenReturn(Optional.of(appointment));

    // Capture the notification being saved
    ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
    when(notificationRepo.save(notificationCaptor.capture())).thenAnswer(invocation -> {
        Notification n = invocation.getArgument(0);
        n.setId(100L); // simulate DB generated ID
        return n;
    });

    // Mock findById to return the same notification that will be saved
    when(notificationRepo.findById(100L)).thenAnswer(invocation -> Optional.of(notificationCaptor.getValue()));

    // Mock email sending
    doNothing().when(emailService).send(any());

    // Act
    notificationService.sendNotification(request);

    // Assert
    verify(emailService, times(1)).send(any());
    Notification savedNotification = notificationCaptor.getValue();
    assertEquals("Reminder", savedNotification.getMessage());
    assertEquals(user, savedNotification.getRecipient());
    }

    @Test
    void testScheduleNotification_Success() {
        when(appointmentRepo.findById(2L)).thenReturn(Optional.of(appointment));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        notificationService.scheduleNotification(request);
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(),anyString(),eq(request),any(MessagePostProcessor.class));
    }
}
