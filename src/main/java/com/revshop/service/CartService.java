package com.revshop.service;

import com.revshop.dao.CartDAO;
import com.revshop.model.CartItem;
import java.util.List;

public class CartService {
    private CartDAO cartDAO = new CartDAO();

    public void addToCart(int buyerId, int productId, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }
        cartDAO.addToCart(buyerId, productId, quantity);
        System.out.println("Product added to cart!");
    }

    public List<CartItem> getCartItems(int buyerId) {
        return cartDAO.getCartItems(buyerId);
    }

    public void clearCart(int buyerId) {
        cartDAO.clearCart(buyerId);
    }

    public void removeFromCart(int buyerId, int productId) {
        cartDAO.removeFromCart(buyerId, productId);
    }

    public void updateCartQuantity(int buyerId, int productId, int quantity) {
        if (quantity <= 0) {
            removeFromCart(buyerId, productId);
        } else {
            cartDAO.updateCartQuantity(buyerId, productId, quantity);
        }
    }
}
