package com.example.SmartAppointmentBookingSystem.service;
import java.util.List;

import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.User;

public interface TenantService {

    List<TenantResponseDTO> getAllTenants();
    TenantResponseDTO getTenantById(Long id);
    TenantResponseDTO addTenant(TenantRequestDTO tenantRequestDTO, User currentUser);
    TenantResponseDTO updateTenant(Long id, TenantRequestDTO tenantRequestDTO, User currentUser);
    void deleteTenant(Long id, User currentUser);
    
}
