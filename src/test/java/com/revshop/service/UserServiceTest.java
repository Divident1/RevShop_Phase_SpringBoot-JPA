package com.revshop.service;

import com.revshop.model.Role;
import com.revshop.model.User;
import com.revshop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testInvalidEmailValidation() {
        User userWithBadEmail = new User("invalidemail", "password123", Role.BUYER, "Test", "1234567890");
        boolean result = userService.registerUser(userWithBadEmail);
        assertFalse(result, "User with invalid email should fail registration");
    }

    @Test
    public void testNullEmailValidation() {
        User userWithNullEmail = new User(null, "password123", Role.BUYER, "Test", "1234567890");
        boolean result = userService.registerUser(userWithNullEmail);
        assertFalse(result, "User with null email should fail registration");
    }

    @Test
    public void testValidEmailFormat() {
        User user = new User("valid@email.com", "password123", Role.BUYER, "Test", "1234567890");
        assertTrue(user.getEmail().contains("@"), "Valid email should contain @");
    }
}
