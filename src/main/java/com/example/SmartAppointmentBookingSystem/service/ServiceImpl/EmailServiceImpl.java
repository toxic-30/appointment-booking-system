package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.SmartAppointmentBookingSystem.dto.notification.EmailMessageDTO;
import com.example.SmartAppointmentBookingSystem.service.EmailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void send(EmailMessageDTO emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailMessage.getTo());
        message.setSubject(emailMessage.getSubject());
        message.setText(emailMessage.getBody());
        message.setFrom("shivangipandey556@gmail.com");
        mailSender.send(message);
    }
}    
