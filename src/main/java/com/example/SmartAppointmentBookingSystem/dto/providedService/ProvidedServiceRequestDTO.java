package com.example.SmartAppointmentBookingSystem.dto.providedService;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvidedServiceRequestDTO {

    private String name;
    private String description;
    private Double price;
    private int durationMinutes;
    private Long tenantId; // link service to a tenant

}
