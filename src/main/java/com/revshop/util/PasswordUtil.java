package com.revshop.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private PasswordUtil() {

    }

    /**
     * Hashes a plain text password using BCrypt.
     * 
     * @param plainPassword The plain text password.
     * @return The hashed password.
     */
    public static String hash(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    public static boolean check(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}
