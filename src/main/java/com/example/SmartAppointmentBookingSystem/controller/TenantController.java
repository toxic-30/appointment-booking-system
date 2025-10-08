package com.example.SmartAppointmentBookingSystem.controller;

import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.UserRole;
import com.example.SmartAppointmentBookingSystem.service.TenantService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    // Everyone can view all tenants
    @GetMapping
    public ResponseEntity<List<TenantResponseDTO>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    // Everyone can view a tenant by ID
    @GetMapping("/{id}")
    public ResponseEntity<TenantResponseDTO> getTenantById(@PathVariable Long id) {
        return ResponseEntity.ok(tenantService.getTenantById(id));
    }

    @PostMapping("/addTenant")
    public ResponseEntity<TenantResponseDTO> addTenant(
            @Valid @RequestBody TenantRequestDTO tenantRequestDTO,
            @AuthenticationPrincipal User currentUser) {
        System.out.println("Current User: " + currentUser);
        if (currentUser.getRole() != UserRole.ADMIN && currentUser.getRole() != UserRole.PROVIDER) {
            throw new AccessDeniedException("Only admins or providers can add tenants");
        }
        TenantResponseDTO saved = tenantService.addTenant(tenantRequestDTO, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Update tenant → ADMIN can update any tenant, PROVIDER can only update their own
    @PutMapping("/updateTenant/{id}")
    public ResponseEntity<TenantResponseDTO> updateTenant(
            @PathVariable Long id,
            @Valid @RequestBody TenantRequestDTO tenantRequestDTO,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser.getRole() == UserRole.CUSTOMER) {
            throw new AccessDeniedException("Customers cannot update tenants");
        }

        return ResponseEntity.ok(tenantService.updateTenant(id, tenantRequestDTO, currentUser));
    }

    // Delete tenant → ADMIN can delete any tenant, PROVIDER can only delete their own
    @DeleteMapping("/deleteTenant/{id}")
    public ResponseEntity<Void> deleteTenant(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser.getRole() == UserRole.CUSTOMER) {
            throw new AccessDeniedException("Customers cannot delete tenants");
        }

        tenantService.deleteTenant(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}

