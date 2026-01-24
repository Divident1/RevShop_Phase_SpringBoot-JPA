package com.revshop.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = new User("test@test.com", "password123", "BUYER", "Test User", "1234567890");

        assertEquals("test@test.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("BUYER", user.getRole());
        assertEquals("Test User", user.getName());
        assertEquals("1234567890", user.getPhoneNumber());
    }

    @Test
    public void testUserWithId() {
        User user = new User(1, "test@test.com", "password123", "SELLER", "Seller Name", "9876543210");

        assertEquals(1, user.getUserId());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("SELLER", user.getRole());
    }

    @Test
    public void testSettersAndGetters() {
        User user = new User();
        user.setUserId(5);
        user.setEmail("new@email.com");
        user.setPassword("newpass");
        user.setRole("BUYER");
        user.setName("New Name");
        user.setPhoneNumber("1111111111");

        assertEquals(5, user.getUserId());
        assertEquals("new@email.com", user.getEmail());
        assertEquals("newpass", user.getPassword());
        assertEquals("BUYER", user.getRole());
        assertEquals("New Name", user.getName());
        assertEquals("1111111111", user.getPhoneNumber());
    }
}
