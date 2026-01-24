package com.revshop.dao;

import com.revshop.model.CartItem;
import com.revshop.model.Product;
import com.revshop.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    private static final Logger log = LoggerFactory.getLogger(CartDAO.class);

    public void addToCart(int buyerId, int productId, int quantity) {
        int cartId = getOrCreateCart(buyerId);
        if (cartId == -1)
            return;

        String checkSql = "SELECT quantity FROM CartItems WHERE cartId = ? AND productId = ?";
        String updateSql = "UPDATE CartItems SET quantity = quantity + ? WHERE cartId = ? AND productId = ?";
        String insertSql = "INSERT INTO CartItems (cartId, productId, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setInt(1, cartId);
            check.setInt(2, productId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                // update existing
                PreparedStatement update = conn.prepareStatement(updateSql);
                update.setInt(1, quantity);
                update.setInt(2, cartId);
                update.setInt(3, productId);
                update.executeUpdate();
                update.close();
            } else {
                // insert new
                PreparedStatement insert = conn.prepareStatement(insertSql);
                insert.setInt(1, cartId);
                insert.setInt(2, productId);
                insert.setInt(3, quantity);
                insert.executeUpdate();
                insert.close();
            }
            check.close();
        } catch (SQLException e) {
            log.error("Add to cart failed", e);
        }
    }

    public List<CartItem> getCartItems(int buyerId) {
        List<CartItem> items = new ArrayList<>();
        int cartId = getOrCreateCart(buyerId);

        String sql = "SELECT ci.*, p.name, p.mrp, p.discountedPrice, p.sellerId " +
                "FROM CartItems ci JOIN Products p ON ci.productId = p.productId " +
                "WHERE ci.cartId = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartItemId(rs.getInt("cartItemId"));
                item.setCartId(rs.getInt("cartId"));
                item.setProductId(rs.getInt("productId"));
                item.setQuantity(rs.getInt("quantity"));

                Product p = new Product();
                p.setProductId(rs.getInt("productId"));
                p.setName(rs.getString("name"));
                p.setMrp(rs.getDouble("mrp"));
                p.setDiscountedPrice(rs.getDouble("discountedPrice"));
                p.setSellerId(rs.getInt("sellerId"));
                item.setProduct(p);

                items.add(item);
            }
        } catch (SQLException e) {
            log.error("Get cart items failed", e);
        }
        return items;
    }

    public void clearCart(int buyerId) {
        int cartId = getOrCreateCart(buyerId);
        String sql = "DELETE FROM CartItems WHERE cartId = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Clear cart failed", e);
        }
    }

    public void removeFromCart(int buyerId, int productId) {
        int cartId = getOrCreateCart(buyerId);
        String sql = "DELETE FROM CartItems WHERE cartId = ? AND productId = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Remove from cart failed", e);
        }
    }

    public void updateCartQuantity(int buyerId, int productId, int newQty) {
        int cartId = getOrCreateCart(buyerId);
        if (newQty <= 0) {
            removeFromCart(buyerId, productId);
            return;
        }

        String sql = "UPDATE CartItems SET quantity = ? WHERE cartId = ? AND productId = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newQty);
            stmt.setInt(2, cartId);
            stmt.setInt(3, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Update cart qty failed", e);
        }
    }

    private int getOrCreateCart(int buyerId) {
        String select = "SELECT cartId FROM Carts WHERE buyerId = ?";
        String insert = "INSERT INTO Carts (buyerId) VALUES (?)";

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(select);
            stmt.setInt(1, buyerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("cartId");
                stmt.close();
                return id;
            }
            stmt.close();

            PreparedStatement ins = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            ins.setInt(1, buyerId);
            ins.executeUpdate();
            ResultSet keys = ins.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                ins.close();
                return id;
            }
            ins.close();
        } catch (SQLException e) {
            log.error("Get/create cart failed", e);
        }
        return -1;
    }
}
