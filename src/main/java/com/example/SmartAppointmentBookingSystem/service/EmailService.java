package com.example.SmartAppointmentBookingSystem.service;

import com.example.SmartAppointmentBookingSystem.dto.notification.EmailMessageDTO;

public interface EmailService {
    void send(EmailMessageDTO emailMessage);
}

