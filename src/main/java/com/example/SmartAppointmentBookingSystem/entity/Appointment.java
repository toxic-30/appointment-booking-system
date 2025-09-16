package com.example.SmartAppointmentBookingSystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.SmartAppointmentBookingSystem.enums.AppointmentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Appointment(Long id, String notes, AppointmentStatus status, User provider, User customer, Service service,
            LocalDate appointmentDate, LocalTime appointmentTime, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.notes = notes;
        this.status = status;
        this.provider = provider;
        this.customer = customer;
        this.service = service;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }
    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
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
                + ", customer=" + customer + ", service=" + service + ", appointmentDate=" + appointmentDate
                + ", appointmentTime=" + appointmentTime + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
                + "]";
    }
    
}
