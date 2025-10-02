package com.example.SmartAppointmentBookingSystem.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SmartAppointmentBookingSystem.entity.ProviderServiceMapping;
import com.example.SmartAppointmentBookingSystem.service.ProviderServiceMappingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mappings")
@RequiredArgsConstructor
public class ProviderServiceMappingController {

    private final ProviderServiceMappingService mappingService;

    @PostMapping("/create")
    public ResponseEntity<ProviderServiceMapping> createMapping(@RequestBody ProviderServiceMapping mapping) {
        return ResponseEntity.ok(mappingService.createMapping(mapping));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderServiceMapping> getMappingById(@PathVariable Long id) {
        ProviderServiceMapping mapping = mappingService.getMappingById(id);
        return ResponseEntity.ok(mapping);
    }

    @GetMapping
    public ResponseEntity<List<ProviderServiceMapping>> getAllMappings() {
        return ResponseEntity.ok(mappingService.getAllMappings());
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<ProviderServiceMapping>> getMappingsByProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(mappingService.getMappingsByProvider(providerId));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<ProviderServiceMapping>> getMappingsByService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(mappingService.getMappingsByService(serviceId));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<ProviderServiceMapping>> getMappingsByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(mappingService.getMappingsByTenant(tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderServiceMapping> updateMapping(@PathVariable Long id, @RequestBody ProviderServiceMapping updatedMapping) {
        return ResponseEntity.ok(mappingService.updateMapping(id, updatedMapping));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMapping(@PathVariable Long id) {
        mappingService.deleteMapping(id);
        return ResponseEntity.noContent().build();
    }
}