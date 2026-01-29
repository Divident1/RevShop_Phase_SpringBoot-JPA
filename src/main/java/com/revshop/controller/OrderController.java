package com.revshop.controller;

import com.revshop.model.Order;
import com.revshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{userId}")
    public ResponseEntity<String> placeOrder(@PathVariable int userId, @RequestBody Map<String, String> payload) {
        String address = payload.get("shippingAddress");
        boolean success = orderService.placeOrder(userId, address);
        if (success) {
            return new ResponseEntity<>("Order placed successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to place order. Cart might be empty.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/buyer/{userId}")
    public ResponseEntity<List<Order>> getOrdersByBuyer(@PathVariable int userId) {
        List<Order> orders = orderService.getOrdersByBuyer(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Order>> getOrdersForSeller(@PathVariable int sellerId) {
        List<Order> orders = orderService.getOrdersForSeller(sellerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable int orderId,
            @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        boolean success = orderService.updateOrderStatus(orderId, status);
        if (success) {
            return new ResponseEntity<>("Order status updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update status", HttpStatus.BAD_REQUEST);
        }
    }
}
