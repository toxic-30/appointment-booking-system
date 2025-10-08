package com.example.SmartAppointmentBookingSystem.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.SmartAppointmentBookingSystem.entity.ProviderServiceMapping;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.service.ProviderServiceMappingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mappings")
@RequiredArgsConstructor
public class ProviderServiceMappingController {

    private final ProviderServiceMappingService mappingService;

    @PostMapping("/create")
    public ResponseEntity<ProviderServiceMapping> createMapping(
            @RequestBody ProviderServiceMapping mapping,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(mappingService.createMapping(mapping, currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderServiceMapping> getMappingById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(mappingService.getMappingById(id, currentUser));
    }

    @GetMapping
    public ResponseEntity<List<ProviderServiceMapping>> getAllMappings(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(mappingService.getAllMappings(currentUser));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProviderServiceMapping> updateMapping(
            @PathVariable Long id,
            @RequestBody ProviderServiceMapping updatedMapping,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(mappingService.updateMapping(id, updatedMapping, currentUser));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMapping(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        mappingService.deleteMapping(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
