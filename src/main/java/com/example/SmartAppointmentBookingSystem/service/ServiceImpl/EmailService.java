package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendEmail(String to, String subject, String body) {
        System.out.println("📧 Sending email to " + to + " | Subject: " + subject + " | Body: " + body);
    }
}