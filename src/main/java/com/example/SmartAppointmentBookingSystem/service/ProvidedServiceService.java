package com.example.SmartAppointmentBookingSystem.service;

import java.util.List;
import com.example.SmartAppointmentBookingSystem.dto.providedService.ProvidedServiceRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.providedService.ProvidedServiceResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.User;

public interface ProvidedServiceService {
    
    List<ProvidedServiceResponseDTO> getAllServices();

    List<ProvidedServiceResponseDTO> getServicesByTenant(Long tenantId);

    ProvidedServiceResponseDTO getServiceById(Long id);

    ProvidedServiceResponseDTO addService(ProvidedServiceRequestDTO serviceRequestDTO, User currentUser);

    ProvidedServiceResponseDTO updateService(Long id, ProvidedServiceRequestDTO serviceRequestDTO, User currentUser);

    void deleteService(Long id, User currentUser);
}
