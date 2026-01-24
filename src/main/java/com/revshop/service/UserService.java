package com.revshop.service;

import com.revshop.dao.UserDAO;
import com.revshop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserDAO userDAO = new UserDAO();

    public boolean registerUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            logger.warn("Invalid email: {}", user.getEmail());
            return false;
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            logger.warn("Password too short");
            return false;
        }
        String role = user.getRole();
        if (role == null || (!role.equalsIgnoreCase("BUYER") && !role.equalsIgnoreCase("SELLER"))) {
            logger.warn("Invalid role: {}", role);
            return false;
        }
        return userDAO.registerUser(user);
    }

    public User loginUser(String email, String password) {
        if (email == null || password == null)
            return null;
        return userDAO.loginUser(email, password);
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
        User user = userDAO.loginUser(email, oldPassword);
        if (user == null) {
            logger.warn("Wrong old password for {}", email);
            return false;
        }
        if (newPassword == null || newPassword.length() < 6) {
            logger.warn("New password too short");
            return false;
        }
        return userDAO.updatePassword(email, newPassword);
    }

    public boolean resetPassword(String email, String newPassword) {
        if (!userDAO.emailExists(email)) {
            logger.warn("Email not found: {}", email);
            return false;
        }
        if (newPassword == null || newPassword.length() < 6) {
            logger.warn("New password too short");
            return false;
        }
        return userDAO.updatePassword(email, newPassword);
    }
}
