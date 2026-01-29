package com.revshop.service;

import com.revshop.model.Product;
import com.revshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public boolean addProduct(Product product) {
        if (product.getDiscountedPrice() > product.getMrp()) {
            System.out.println("Error: Discounted price cannot be greater than MRP.");
            return false;
        }
        try {
            productRepository.save(product);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsBySeller(int sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Product> searchByCategory(int categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * Updates an existing product.
     * Checks if the discounted price is valid (must be <= MRP).
     * 
     * @param product The product with updated details.
     * @return true if update is successful, false otherwise.
     */
    @Transactional
    public boolean updateProduct(Product product) {
        // Business Logic: Validate price integrity
        if (product.getDiscountedPrice() > product.getMrp()) {
            System.out.println("Error: Discounted price cannot be greater than MRP.");
            return false;
        }
        try {
            // Check existence before update to prevent phantom saves if using save() on new
            // ID
            if (productRepository.existsById(product.getProductId())) {
                productRepository.save(product);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int productId, int sellerId) {
        try {
            // Check if product exists and belongs to seller
            Product p = productRepository.findById(productId).orElse(null);
            if (p != null && p.getSellerId() == sellerId) {
                productRepository.deleteById(productId);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
