package com.example.SmartAppointmentBookingSystem.dto.providedService;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvidedServiceResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private int durationMinutes;
    private Long tenantId;
    private String tenantName;

}
