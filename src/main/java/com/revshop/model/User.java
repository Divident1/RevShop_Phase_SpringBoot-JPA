package com.revshop.model;

public class User {
    private int userId;
    private String email;
    private String password;
    private String role;
    private String name;
    private String phoneNumber;

    public User() {
    }

    public User(int userId, String email, String password, String role, String name, String phoneNumber) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public User(String email, String password, String role, String name, String phoneNumber) {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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

    // Temporary fields for registration
    private String businessName;
    private String gstin;

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
