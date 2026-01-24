package com.revshop.dao;

import com.revshop.model.Notification;
import com.revshop.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    public void sendNotification(int userId, String message) {
        String sql = "INSERT INTO Notifications (userId, message) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getUnreadNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE userId = ? AND isRead = FALSE";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Notification n = new Notification();
                n.setNotificationId(rs.getInt("notificationId"));
                n.setUserId(rs.getInt("userId"));
                n.setMessage(rs.getString("message"));
                n.setRead(rs.getBoolean("isRead"));
                n.setCreatedAt(rs.getTimestamp("createdAt")); // Fixed column name
                notifications.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public void markAsRead(int userId) {
        String sql = "UPDATE Notifications SET isRead = TRUE WHERE userId = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
