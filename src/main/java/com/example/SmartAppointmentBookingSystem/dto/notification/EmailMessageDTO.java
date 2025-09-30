package com.example.SmartAppointmentBookingSystem.dto.notification;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageDTO {

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid recipient email format")
    private String to;

    @NotBlank(message = "Email subject is required")
    @Size(max = 255, message = "Subject must be under 255 characters")
    private String subject;

    @NotBlank(message = "Email body is required")
    private String body;

}