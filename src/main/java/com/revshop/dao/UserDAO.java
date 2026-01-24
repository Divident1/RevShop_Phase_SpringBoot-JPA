package com.revshop.dao;

import com.revshop.model.User;
import com.revshop.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;

public class UserDAO {

    private static final Logger log = LoggerFactory.getLogger(UserDAO.class);

    public boolean registerUser(User user) {
        String countSql = "SELECT COUNT(*) FROM Users WHERE email = ?";
        String userSql = "INSERT INTO Users (email, password, role, name, phone) VALUES (?, ?, ?, ?, ?)";
        String buyerSql = "INSERT INTO Buyers (userId, shippingAddress, billingAddress) VALUES (?, '', '')";
        String sellerSql = "INSERT INTO Sellers (userId, businessName, gstin) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();

            // check if email exists
            PreparedStatement checkStmt = conn.prepareStatement(countSql);
            checkStmt.setString(1, user.getEmail());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                log.warn("Email already exists: {}", user.getEmail());
                checkStmt.close();
                return false;
            }
            checkStmt.close();

            conn.setAutoCommit(false);

            int userId = -1;
            PreparedStatement stmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getName());
            stmt.setString(5, user.getPhoneNumber());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                stmt.close();
                return false;
            }

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                userId = keys.getInt(1);
                user.setUserId(userId);
            }
            stmt.close();

            // insert into buyer or seller table
            if ("BUYER".equalsIgnoreCase(user.getRole())) {
                PreparedStatement buyerStmt = conn.prepareStatement(buyerSql);
                buyerStmt.setInt(1, userId);
                buyerStmt.executeUpdate();
                buyerStmt.close();
            } else if ("SELLER".equalsIgnoreCase(user.getRole())) {
                PreparedStatement sellerStmt = conn.prepareStatement(sellerSql);
                sellerStmt.setInt(1, userId);
                sellerStmt.setString(2, user.getBusinessName() != null ? user.getBusinessName() : "");
                sellerStmt.setString(3, user.getGstin() != null ? user.getGstin() : "");
                sellerStmt.executeUpdate();
                sellerStmt.close();
            }

            conn.commit();
            log.info("User registered: {} (ID: {})", user.getEmail(), userId);
            return true;

        } catch (SQLException e) {
            log.error("Registration error: {}", e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public User loginUser(String email, String password) {
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                log.info("Login success: {}", email);
                return new User(
                        rs.getInt("userId"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("name"),
                        rs.getString("phone"));
            }
            log.warn("Login failed: {}", email);
        } catch (SQLException e) {
            log.error("Login error", e);
        }
        return null;
    }

    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE Users SET password = ? WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, email);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Password update failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean checkPassword(int userId, String password) {
        String sql = "SELECT COUNT(*) FROM Users WHERE userId = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            log.error("Check password error", e);
        }
        return false;
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM Users WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            log.error("Email check error", e);
        }
        return false;
    }
}
