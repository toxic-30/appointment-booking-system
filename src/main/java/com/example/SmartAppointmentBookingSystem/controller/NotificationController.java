package com.example.SmartAppointmentBookingSystem.controller;

import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationResponseDTO;
import com.example.SmartAppointmentBookingSystem.enums.NotificationStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SmartAppointmentBookingSystem.service.NotificationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Get notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }
    // Create new notification
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(
            @RequestBody NotificationRequestDTO requestDTO,
            @RequestParam NotificationStatus status
    ) {
        return ResponseEntity.ok(notificationService.createNotification(requestDTO, status));
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequestDTO requestDTO) {
        notificationService.sendNotification(requestDTO);
        return ResponseEntity.ok().build();
    }
    // Schedule notification via RabbitMQ (for reminders)
    @PostMapping("/schedule")
    public ResponseEntity<Void> scheduleNotification(@RequestBody NotificationRequestDTO requestDTO) {
        notificationService.scheduleNotification(requestDTO);
        return ResponseEntity.ok().build();
    }
    // Update notification status (e.g. SENT, FAILED, READ)
    @PutMapping("/{id}/status")
    public ResponseEntity<NotificationResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(notificationService.updateStatus(id, status));
    }
}
