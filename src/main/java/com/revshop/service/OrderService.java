package com.revshop.service;

import com.revshop.dao.CartDAO;
import com.revshop.dao.OrderDAO;
import com.revshop.dao.ProductDAO;
import com.revshop.dao.NotificationDAO;
import com.revshop.model.CartItem;
import com.revshop.model.Order;
import com.revshop.util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private CartDAO cartDAO = new CartDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private ProductDAO productDAO = new ProductDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();

    public boolean placeOrder(int buyerId, String shippingAddress) {
        List<CartItem> cartItems = cartDAO.getCartItems(buyerId);
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty!");
            return false;
        }

        double totalAmount = 0;
        for (CartItem item : cartItems) {
            totalAmount += item.getProduct().getDiscountedPrice() * item.getQuantity();
        }

        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setTotalAmount(totalAmount);
        order.setShippingAddress(shippingAddress);

        int orderId = orderDAO.createOrder(order);
        if (orderId != -1) {
            for (CartItem item : cartItems) {
                orderDAO.createOrderItem(orderId, item.getProductId(), item.getQuantity(),
                        item.getProduct().getDiscountedPrice());

                // Critical Fix: Deduct stock
                boolean stockUpdated = productDAO.updateProductStock(item.getProductId(), item.getQuantity());
                if (!stockUpdated) {
                    System.out.println("Warning: Failed to update stock for Product ID: " + item.getProductId());
                }

                // Notify Seller
                notificationDAO.sendNotification(item.getProduct().getSellerId(),
                        "New Order #" + orderId + " received for product: " + item.getProduct().getName());
            }

            // Notify Buyer
            notificationDAO.sendNotification(buyerId, "Order #" + orderId + " placed successfully!");

            cartDAO.clearCart(buyerId);
            return true;
        }
        return false;
    }

    public List<Order> getOrderHistory(int buyerId) {
        return orderDAO.getOrdersByBuyer(buyerId);
    }

    public List<Order> getOrdersForSeller(int sellerId) {
        return orderDAO.getOrdersBySeller(sellerId);
    }
}
