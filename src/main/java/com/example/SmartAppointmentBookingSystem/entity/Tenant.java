package com.example.SmartAppointmentBookingSystem.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Tenant {
    @Id
    public Long id;
    public String name;
    
}
