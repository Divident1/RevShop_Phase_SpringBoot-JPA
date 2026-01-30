package com.revshop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone")
    private String phoneNumber;

    public User() {
    }

    public User(int userId, String email, String password, Role role, String name, String phoneNumber) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public User(String email, String password, Role role, String name, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", email=" + email + ", role=" + role + ", name=" + name + "]";
    }

    // Temporary fields for registration - Not in Users table
    @Transient
    private String businessName; // stored in Sellers table manually for now
    @Transient
    private String gstin; // stored in Sellers table manually for now

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }
}
