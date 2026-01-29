package com.revshop.service;

import com.revshop.model.Cart;
import com.revshop.model.CartItem;
import com.revshop.repository.CartItemRepository;
import com.revshop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public void addToCart(int buyerId, int productId, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }

        Cart cart = getOrCreateCart(buyerId);
        Optional<CartItem> itemOpt = cartItemRepository.findByCartIdAndProductId(cart.getCartId(), productId);

        if (itemOpt.isPresent()) {
            CartItem item = itemOpt.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCartId(cart.getCartId());
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
        System.out.println("Product added to cart!");
    }

    public List<CartItem> getCartItems(int buyerId) {
        Optional<Cart> cartOpt = cartRepository.findByBuyerId(buyerId);
        if (cartOpt.isEmpty()) {
            return new ArrayList<>();
        }
        return cartItemRepository.findByCartId(cartOpt.get().getCartId());
    }

    @Transactional
    public void clearCart(int buyerId) {
        Optional<Cart> cartOpt = cartRepository.findByBuyerId(buyerId);
        if (cartOpt.isPresent()) {
            cartItemRepository.deleteByCartId(cartOpt.get().getCartId());
        }
    }

    @Transactional
    public void removeFromCart(int buyerId, int productId) {
        Optional<Cart> cartOpt = cartRepository.findByBuyerId(buyerId);
        if (cartOpt.isPresent()) {
            cartItemRepository.deleteByCartIdAndProductId(cartOpt.get().getCartId(), productId);
        }
    }

    @Transactional
    public void updateCartQuantity(int buyerId, int productId, int quantity) {
        if (quantity <= 0) {
            removeFromCart(buyerId, productId);
        } else {
            Optional<Cart> cartOpt = cartRepository.findByBuyerId(buyerId);
            if (cartOpt.isPresent()) {
                Optional<CartItem> itemOpt = cartItemRepository.findByCartIdAndProductId(cartOpt.get().getCartId(),
                        productId);
                if (itemOpt.isPresent()) {
                    CartItem item = itemOpt.get();
                    item.setQuantity(quantity);
                    cartItemRepository.save(item);
                }
            }
        }
    }

    @Transactional
    public void updateCartItemQuantity(int cartItemId, int quantity) {
        if (quantity <= 0) {
            removeCartItem(cartItemId);
        } else {
            Optional<CartItem> itemOpt = cartItemRepository.findById(cartItemId);
            if (itemOpt.isPresent()) {
                CartItem item = itemOpt.get();
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        }
    }

    @Transactional
    public void removeCartItem(int cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    private Cart getOrCreateCart(int buyerId) {
        return cartRepository.findByBuyerId(buyerId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setBuyerId(buyerId);
                    return cartRepository.save(newCart);
                });
    }
}
