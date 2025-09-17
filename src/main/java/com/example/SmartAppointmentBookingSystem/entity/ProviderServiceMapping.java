package com.example.SmartAppointmentBookingSystem.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import jakarta.persistence.*;

@Entity
public class ProviderServiceMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider; 
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
    private Double priceOverride;
    private Integer durationOverrideMinutes;
    private Boolean isActive = true;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public ProviderServiceMapping(Long id, User provider, Service service, Tenant tenant, Double priceOverride,
            Integer durationOverrideMinutes, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.provider = provider;
        this.service = service;
        this.tenant = tenant;
        this.priceOverride = priceOverride;
        this.durationOverrideMinutes = durationOverrideMinutes;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public ProviderServiceMapping() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Double getPriceOverride() {
        return priceOverride;
    }

    public void setPriceOverride(Double priceOverride) {
        this.priceOverride = priceOverride;
    }

    public Integer getDurationOverrideMinutes() {
        return durationOverrideMinutes;
    }

    public void setDurationOverrideMinutes(Integer durationOverrideMinutes) {
        this.durationOverrideMinutes = durationOverrideMinutes;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ProviderServiceMapping [id=" + id + ", provider=" + provider + ", service=" + service + ", tenant="
                + tenant + ", priceOverride=" + priceOverride + ", durationOverrideMinutes=" + durationOverrideMinutes
                + ", isActive=" + isActive + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
