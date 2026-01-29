package com.revshop.controller;

import com.revshop.model.CartItem;
import com.revshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<String> addToCart(@PathVariable int userId, @RequestBody Map<String, Integer> payload) {
        int productId = payload.get("productId");
        int quantity = payload.get("quantity");
        cartService.addToCart(userId, productId, quantity);
        return new ResponseEntity<>("Added to cart", HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable int userId) {
        List<CartItem> items = cartService.getCartItems(userId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<String> updateQuantity(@PathVariable int cartItemId,
            @RequestBody Map<String, Integer> payload) {
        int quantity = payload.get("quantity");
        cartService.updateCartItemQuantity(cartItemId, quantity);
        return new ResponseEntity<>("Quantity updated", HttpStatus.OK);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<String> removeItem(@PathVariable int cartItemId) {
        cartService.removeCartItem(cartItemId);
        return new ResponseEntity<>("Item removed", HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable int userId) {
        cartService.clearCart(userId);
        return new ResponseEntity<>("Cart cleared", HttpStatus.OK);
    }
}
