package com.revshop.service;

import com.revshop.model.User;
import com.revshop.repository.UserRepository;
import com.revshop.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public boolean registerUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            LoggerUtil.warn("Invalid email: {}", user.getEmail());
            return false;
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            LoggerUtil.warn("Password too short");
            return false;
        }
        String role = user.getRole();
        if (role == null || (!role.equalsIgnoreCase("BUYER") && !role.equalsIgnoreCase("SELLER"))) {
            LoggerUtil.warn("Invalid role: {}", role);
            return false;
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            LoggerUtil.warn("Email already exists: {}", user.getEmail());
            return false;
        }

        try {
            userRepository.save(user);
            LoggerUtil.info("User registered: {}", user.getEmail());
            return true;
        } catch (Exception e) {
            LoggerUtil.error("Registration error", e);
            return false;
        }
    }

    public User loginUser(String email, String password) {
        if (email == null || password == null)
            return null;

        Optional<User> userOpt = userRepository.findByEmailAndPassword(email, password);
        if (userOpt.isPresent()) {
            LoggerUtil.info("Login success: {}", email);
            return userOpt.get();
        } else {
            LoggerUtil.warn("Login failed: {}", email);
            return null;
        }
    }

    @Transactional
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmailAndPassword(email, oldPassword);
        if (userOpt.isEmpty()) {
            LoggerUtil.warn("Wrong old password for {}", email);
            return false;
        }
        if (newPassword == null || newPassword.length() < 6) {
            LoggerUtil.warn("New password too short");
            return false;
        }

        User user = userOpt.get();
        user.setPassword(newPassword);
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean resetPassword(String email, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            LoggerUtil.warn("Email not found: {}", email);
            return false;
        }
        if (newPassword == null || newPassword.length() < 6) {
            LoggerUtil.warn("New password too short");
            return false;
        }

        User user = userOpt.get();
        user.setPassword(newPassword);
        userRepository.save(user); // JPA detects change on managed entity
        return true;
    }
}
