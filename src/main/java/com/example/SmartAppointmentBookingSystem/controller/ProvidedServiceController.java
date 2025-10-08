package com.example.SmartAppointmentBookingSystem.controller;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.SmartAppointmentBookingSystem.dto.providedService.ProvidedServiceRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.providedService.ProvidedServiceResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.service.ProvidedServiceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/services")
public class ProvidedServiceController {

    private final ProvidedServiceService providedServiceService;

    // Get all services
    @GetMapping("/getAllServices")
    public ResponseEntity<List<ProvidedServiceResponseDTO>> getAllServices() {
        return ResponseEntity.ok(providedServiceService.getAllServices());
    }

    // Get services by tenant
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<ProvidedServiceResponseDTO>> getServicesByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(providedServiceService.getServicesByTenant(tenantId));
    }

    // Get service by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProvidedServiceResponseDTO> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(providedServiceService.getServiceById(id));
    }

    // Add new service
    @PostMapping("/addService")
    public ResponseEntity<ProvidedServiceResponseDTO> addService(@RequestBody ProvidedServiceRequestDTO requestDTO,
    @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(providedServiceService.addService(requestDTO, currentUser));
    }

    // Update service
    @PutMapping("/updateService/{id}")
    public ResponseEntity<ProvidedServiceResponseDTO> updateService(
            @PathVariable Long id,
            @RequestBody ProvidedServiceRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(providedServiceService.updateService(id, requestDTO, currentUser));
    }

    //  Delete service
    @DeleteMapping("/deleteService/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id,@AuthenticationPrincipal User currentUser) {
        providedServiceService.deleteService(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
