package com.example.SmartAppointmentBookingSystem.service;

import java.util.List;
import com.example.SmartAppointmentBookingSystem.dto.providedService.ProvidedServiceRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.providedService.ProvidedServiceResponseDTO;

public interface ProvidedServiceService {
    
    List<ProvidedServiceResponseDTO> getAllServices();

    List<ProvidedServiceResponseDTO> getServicesByTenant(Long tenantId);

    ProvidedServiceResponseDTO getServiceById(Long id);

    ProvidedServiceResponseDTO addService(ProvidedServiceRequestDTO serviceRequestDTO);

    ProvidedServiceResponseDTO updateService(Long id, ProvidedServiceRequestDTO serviceRequestDTO);

    void deleteService(Long id);
}
