package com.revshop.dao;

import com.revshop.model.Order;
import com.revshop.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private static final Logger LOG = LoggerFactory.getLogger(OrderDAO.class);

    public int createOrder(Order order) {
        String sql = "INSERT INTO Orders (buyerId, totalAmount, status, shippingAddress) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getBuyerId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setString(3, "PENDING");
            stmt.setString(4, order.getShippingAddress());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                LOG.info("Order created: {}", id);
                return id;
            }
        } catch (SQLException e) {
            LOG.error("Create order failed", e);
        }
        return -1;
    }

    public void createOrderItem(int orderId, int productId, int quantity, double price) {
        String sql = "INSERT INTO OrderItems (orderId, productId, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Create order item failed", e);
        }
    }

    public List<Order> getOrdersByBuyer(int buyerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE buyerId = ? ORDER BY orderDate DESC";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, buyerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            LOG.error("Get buyer orders failed", e);
        }
        return orders;
    }

    public List<Order> getOrdersBySeller(int sellerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT DISTINCT o.* FROM Orders o " +
                "JOIN OrderItems oi ON o.orderId = oi.orderId " +
                "JOIN Products p ON oi.productId = p.productId " +
                "WHERE p.sellerId = ? ORDER BY o.orderDate DESC";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            LOG.error("Get seller orders failed", e);
        }
        return orders;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("orderId"));
        o.setBuyerId(rs.getInt("buyerId"));
        o.setOrderDate(rs.getTimestamp("orderDate"));
        o.setTotalAmount(rs.getDouble("totalAmount"));
        o.setStatus(rs.getString("status"));
        try {
            o.setShippingAddress(rs.getString("shippingAddress"));
        } catch (SQLException e) {
            // column might not exist
        }
        return o;
    }
}
