package com.example.SmartAppointmentBookingSystem.entity;
import com.example.SmartAppointmentBookingSystem.enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userCode; // Unique code for the user

    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
    public User(Long id, String userCode, String name, String email, String password, Role role, Tenant tenant) {
        this.id = id;
        this.userCode = userCode;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.tenant = tenant;
    }
    public User(){

    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String setUserCode() {
        return userCode;
    }
    public String getUserCode() {
        return userCode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public Tenant getTenant() {
        return tenant;
    }
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", usercode=" + userCode +", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
                + ", tenant=" + tenant + "]";
    }
}
