package com.revshop.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    private int buyerId;
    private Timestamp orderDate;
    private double totalAmount;
    private String status;
    private String shippingAddress;

    public Order() {
    }

    public Order(int orderId, int buyerId, Timestamp orderDate, double totalAmount, String status) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Override
    public String toString() {
        return String.format("Order #%d | Date: %s | Total: %.2f | Status: %s | Ship To: %s",
                orderId, orderDate, totalAmount, status, shippingAddress != null ? shippingAddress : "N/A");
    }
}
