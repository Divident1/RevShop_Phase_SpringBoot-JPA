package com.revshop.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.revshop.model.Product;
import com.revshop.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FavoriteDAO {

    private static final Logger log = LoggerFactory.getLogger(FavoriteDAO.class);

    public boolean addFavorite(int userId, int productId) {
        // check if already exists
        String checkSql = "SELECT 1 FROM Favorites WHERE userId = ? AND productId = ?";
        String sql = "INSERT INTO Favorites (userId, productId) VALUES (?, ?)";

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setInt(1, userId);
            check.setInt(2, productId);
            if (check.executeQuery().next()) {
                log.info("Product {} already in favorites for user {}", productId, userId);
                return false;
            }
            check.close();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            boolean added = stmt.executeUpdate() > 0;
            stmt.close();
            if (added)
                log.info("Added product {} to favorites for user {}", productId, userId);
            return added;
        } catch (SQLException e) {
            log.error("Add favorite failed", e);
            return false;
        }
    }

    public List<Integer> getFavoriteProductIds(int userId) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT productId FROM Favorites WHERE userId = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("productId"));
            }
        } catch (SQLException e) {
            log.error("Get favorites failed", e);
        }
        return ids;
    }

    public List<Product> getFavoriteProducts(int userId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.* FROM Products p " +
                "JOIN Favorites f ON p.productId = f.productId " +
                "WHERE f.userId = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("productId"),
                        rs.getInt("sellerId"),
                        rs.getInt("categoryId"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("mrp"),
                        rs.getDouble("discountedPrice"),
                        rs.getInt("quantity"),
                        rs.getInt("threshold")));
            }
        } catch (SQLException e) {
            log.error("Get favorite products failed", e);
        }
        return products;
    }

    public boolean removeFavorite(int userId, int productId) {
        String sql = "DELETE FROM Favorites WHERE userId = ? AND productId = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Remove favorite failed", e);
            return false;
        }
    }
}
