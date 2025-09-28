package com.example.SmartAppointmentBookingSystem.entity;

import java.time.LocalDateTime;
import com.example.SmartAppointmentBookingSystem.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String notes;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.PENDING;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ProvidedService service;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
    @Column(nullable = false)
    private LocalDateTime appointmentTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Appointment [id=" + id +
                ", status=" + status +
                ", providerId=" + (provider != null ? provider.getId() : null) +
                ", customerId=" + (customer != null ? customer.getId() : null) +
                ", serviceId=" + (service != null ? service.getId() : null) +
                ", tenantId=" + (tenant != null ? tenant.getId() : null) +
                ", appointmentTime=" + appointmentTime +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt + "]";
    }
}
