package com.revshop.dao;

import com.revshop.model.Product;
import com.revshop.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private Logger logger = LoggerFactory.getLogger(ProductDAO.class);

    public boolean addProduct(Product product) {
        String sql = "INSERT INTO Products (sellerId, categoryId, name, description, mrp, discountedPrice, quantity, threshold) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getSellerId());
            stmt.setInt(2, product.getCategoryId());
            stmt.setString(3, product.getName());
            stmt.setString(4, product.getDescription());
            stmt.setDouble(5, product.getMrp());
            stmt.setDouble(6, product.getDiscountedPrice());
            stmt.setInt(7, product.getQuantity());
            stmt.setInt(8, product.getThreshold());

            boolean ok = stmt.executeUpdate() > 0;
            if (ok)
                logger.info("Product added: {}", product.getName());
            return ok;
        } catch (SQLException e) {
            logger.error("Add product failed: {}", e.getMessage());
            return false;
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE quantity > 0";

        try (Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            logger.error("Get all products failed", e);
        }
        return products;
    }

    public List<Product> getProductsBySeller(int sellerId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE sellerId = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            logger.error("Get seller products failed", e);
        }
        return products;
    }

    public List<Product> searchProducts(String query) {
        List<Product> products = new ArrayList<>();
        if (query == null || query.trim().isEmpty())
            return products;

        String sql = "SELECT * FROM Products WHERE (name LIKE ? OR description LIKE ?) AND quantity > 0";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String q = "%" + query.trim() + "%";
            stmt.setString(1, q);
            stmt.setString(2, q);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            logger.error("Search failed", e);
        }
        return products;
    }

    public List<Product> searchByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE categoryId = ? AND quantity > 0";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            logger.error("Category search failed", e);
        }
        return products;
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("productId"),
                rs.getInt("sellerId"),
                rs.getInt("categoryId"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("mrp"),
                rs.getDouble("discountedPrice"),
                rs.getInt("quantity"),
                rs.getInt("threshold"));
    }

    public boolean updateProductStock(int productId, int qty) {
        String sql = "UPDATE Products SET quantity = quantity - ? WHERE productId = ? AND quantity >= ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, qty);
            stmt.setInt(2, productId);
            stmt.setInt(3, qty);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Stock update failed for product {}", productId, e);
            return false;
        }
    }

    public boolean updateProduct(Product p) {
        String sql = "UPDATE Products SET name=?, description=?, mrp=?, discountedPrice=?, quantity=?, threshold=?, categoryId=? WHERE productId=? AND sellerId=?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            stmt.setDouble(3, p.getMrp());
            stmt.setDouble(4, p.getDiscountedPrice());
            stmt.setInt(5, p.getQuantity());
            stmt.setInt(6, p.getThreshold());
            stmt.setInt(7, p.getCategoryId());
            stmt.setInt(8, p.getProductId());
            stmt.setInt(9, p.getSellerId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Update product failed", e);
            return false;
        }
    }

    public boolean deleteProduct(int productId, int sellerId) {
        String sql = "DELETE FROM Products WHERE productId=? AND sellerId=?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, sellerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Delete product failed", e);
            return false;
        }
    }

    public Product getProductById(int productId) {
        String sql = "SELECT * FROM Products WHERE productId = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapProduct(rs);
            }
        } catch (SQLException e) {
            logger.error("Get product by id failed", e);
        }
        return null;
    }
}
