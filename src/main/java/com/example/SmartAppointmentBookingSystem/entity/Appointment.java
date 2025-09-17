package com.example.SmartAppointmentBookingSystem.entity;

import java.time.LocalDateTime;
import com.example.SmartAppointmentBookingSystem.enums.AppointmentStatus;
import jakarta.persistence.*;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String notes;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    private LocalDateTime appointmentDateAndTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Appointment(Long id, String notes, AppointmentStatus status, User provider, User customer, Service service,
            LocalDateTime appointmentDateAndTime, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.notes = notes;
        this.status = status;
        this.provider = provider;
        this.customer = customer;
        this.service = service;
        this.appointmentDateAndTime = appointmentDateAndTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Appointment(){
        
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public AppointmentStatus getStatus() {
        return status;
    }
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
    public User getProvider() {
        return provider;
    }
    public void setProvider(User provider) {
        this.provider = provider;
    }
    public User getCustomer() {
        return customer;
    }
    public void setCustomer(User customer) {
        this.customer = customer;
    }
    public Service getService() {
        return service;
    }
    public void setService(Service service) {
        this.service = service;
    }
    public LocalDateTime getAppointmentDateAndTime() {
        return appointmentDateAndTime;
    }
    public void setAppointmentDateAndTime(LocalDateTime appointmentDateAndTime) {
        this.appointmentDateAndTime = appointmentDateAndTime;
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
        return "Appointment [id=" + id + ", notes=" + notes + ", status=" + status + ", provider=" + provider
                + ", customer=" + customer + ", service=" + service + ", appointmentDateAndTime=" + appointmentDateAndTime
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
                + "]";
    }
    
}
