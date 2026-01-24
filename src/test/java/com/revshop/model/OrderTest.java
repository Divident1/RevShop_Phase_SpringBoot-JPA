package com.revshop.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Timestamp;

public class OrderTest {

    @Test
    public void testOrderCreation() {
        Order order = new Order();
        order.setOrderId(1);
        order.setBuyerId(10);
        order.setTotalAmount(250.50);
        order.setStatus("PENDING");
        order.setShippingAddress("123 Test Street");

        assertEquals(1, order.getOrderId());
        assertEquals(10, order.getBuyerId());
        assertEquals(250.50, order.getTotalAmount());
        assertEquals("PENDING", order.getStatus());
        assertEquals("123 Test Street", order.getShippingAddress());
    }

//    @Test
//    public void testOrderToString() {
//        Order order = new Order();
//        order.setOrderId(5);
//        order.setTotalAmount(100.0);
//        order.setStatus("SHIPPED");
//        order.setShippingAddress("456 Main St");
//
//        String result = order.toString();
//        assertTrue(result.contains("Order #5"));
//        assertTrue(result.contains("SHIPPED"));
//        assertTrue(result.contains("456 Main St"));
//    }

    @Test
    public void testOrderWithNullAddress() {
        Order order = new Order();
        order.setOrderId(1);
        order.setShippingAddress(null);

        String result = order.toString();
        assertTrue(result.contains("N/A"));
    }
}
