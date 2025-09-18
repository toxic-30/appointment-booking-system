package com.example.SmartAppointmentBookingSystem.service;
import java.util.List;

import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantResponseDTO;

public interface TenantService {

    List<TenantResponseDTO> getAllTenants();
    TenantResponseDTO getTenantById(Long id);
    TenantResponseDTO addTenant(TenantRequestDTO tenantRequestDTO);
    TenantResponseDTO updateTenant(Long id, TenantRequestDTO tenantRequestDTO);
    void deleteTenant(Long id);
    
}
