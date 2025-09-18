package com.example.SmartAppointmentBookingSystem.controller;

import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantResponseDTO;
import com.example.SmartAppointmentBookingSystem.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    // Get all tenants
    @GetMapping
    public ResponseEntity<List<TenantResponseDTO>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }
    // Get tenant by ID
    @GetMapping("/{id}")
    public ResponseEntity<TenantResponseDTO> getTenantById(@PathVariable Long id) {
        return ResponseEntity.ok(tenantService.getTenantById(id));
    }
    // Add new tenant
    @PostMapping
    public ResponseEntity<TenantResponseDTO> addTenant(@RequestBody TenantRequestDTO tenantRequestDTO) {
        return ResponseEntity.ok(tenantService.addTenant(tenantRequestDTO));
    }
    // Update tenant
    @PutMapping("/{id}")
    public ResponseEntity<TenantResponseDTO> updateTenant(
            @PathVariable Long id,
            @RequestBody TenantRequestDTO tenantRequestDTO
    ) {
        return ResponseEntity.ok(tenantService.updateTenant(id, tenantRequestDTO));
    }
    // Delete tenant
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return ResponseEntity.noContent().build();
    }
}

