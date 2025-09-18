package com.example.SmartAppointmentBookingSystem.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderServiceMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider; 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ProvidedService service;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
    private Double priceOverride;
    private Integer durationOverrideMinutes;
    private Boolean isActive = true;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "ProviderServiceMapping{" +
                "id=" + id +
                ", providerId=" + (provider != null ? provider.getId() : null) +
                ", serviceId=" + (service != null ? service.getId() : null) +
                ", tenantId=" + (tenant != null ? tenant.getId() : null) +
                ", priceOverride=" + priceOverride +
                ", durationOverrideMinutes=" + durationOverrideMinutes +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}    