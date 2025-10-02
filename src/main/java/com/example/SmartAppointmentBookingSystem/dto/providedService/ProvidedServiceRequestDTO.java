package com.example.SmartAppointmentBookingSystem.dto.providedService;

import lombok.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvidedServiceRequestDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    @Min(value = 0, message = "Price must be non-negative")
    private Double price;
    @Min(value = 0, message = "Duration must be non-negative")
    private int durationMinutes;
    @NotNull
    private Long tenantId; // link service to a tenant
    @NotNull
    private Long userId;

}
