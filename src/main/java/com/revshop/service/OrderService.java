package com.revshop.service;

import com.revshop.model.*;
import com.revshop.repository.NotificationRepository;
import com.revshop.repository.OrderItemRepository;
import com.revshop.repository.OrderRepository;
import com.revshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CartService cartService;

    @Transactional
    public boolean placeOrder(int buyerId, String shippingAddress) {
        List<CartItem> cartItems = cartService.getCartItems(buyerId);
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty!");
            return false;
        }

        double totalAmount = 0;

        // Re-fetch product to get price and stock
        // Actually, let's iterate and sum up.
        for (CartItem item : cartItems) {
            Product p = productRepository.findById(item.getProductId()).orElse(null);
            if (p == null || p.getQuantity() < item.getQuantity()) {
                System.out.println("Product not available: " + item.getProductId());
                return false;
            }
            totalAmount += p.getDiscountedPrice() * item.getQuantity();
        }

        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setTotalAmount(totalAmount);
        order.setShippingAddress(shippingAddress);
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        order.setStatus("Placed");

        Order savedOrder = orderRepository.save(order);
        int orderId = savedOrder.getOrderId();

        for (CartItem item : cartItems) {
            Product p = productRepository.findById(item.getProductId()).orElseThrow();

            OrderItem orderItem = new OrderItem(orderId, item.getProductId(), item.getQuantity(),
                    p.getDiscountedPrice());
            orderItemRepository.save(orderItem);

            // Deduct stock
            p.setQuantity(p.getQuantity() - item.getQuantity());
            productRepository.save(p);

            // Notify Seller
            Notification notif = new Notification(p.getSellerId(),
                    "New Order #" + orderId + " received for product: " + p.getName());
            notif.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            notificationRepository.save(notif);
        }

        // Notify Buyer
        Notification buyerNotif = new Notification(buyerId, "Order #" + orderId + " placed successfully!");
        buyerNotif.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        notificationRepository.save(buyerNotif);

        cartService.clearCart(buyerId);
        return true;
    }

    public List<Order> getOrdersByBuyer(int buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    @Transactional
    public boolean updateOrderStatus(int orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
            // Notify buyer
            Notification notif = new Notification(order.getBuyerId(),
                    "Order #" + orderId + " status updated to: " + status);
            notif.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            notificationRepository.save(notif);
            return true;
        }
        return false;
    }

    public List<Order> getOrdersForSeller(int sellerId) {
        return orderRepository.findOrdersBySellerId(sellerId);
    }
}
