package com.revshop.service;

import com.revshop.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService = new UserService();

    @Test
    public void testInvalidEmailValidation() {
        User userWithBadEmail = new User("invalidemail", "password123", "BUYER", "Test", "1234567890");
        boolean result = userService.registerUser(userWithBadEmail);
        assertFalse(result, "User with invalid email should fail registration");
    }

    @Test
    public void testNullEmailValidation() {
        User userWithNullEmail = new User(null, "password123", "BUYER", "Test", "1234567890");
        boolean result = userService.registerUser(userWithNullEmail);
        assertFalse(result, "User with null email should fail registration");
    }

    @Test
    public void testValidEmailFormat() {
        User user = new User("valid@email.com", "password123", "BUYER", "Test", "1234567890");
        assertTrue(user.getEmail().contains("@"), "Valid email should contain @");
    }
}
