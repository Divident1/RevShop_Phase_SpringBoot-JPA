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

    public Product addProduct(Product product) {
        if (product.getDiscountedPrice() > product.getMrp()) {
            throw new IllegalArgumentException("Discounted price cannot be greater than MRP.");
        }
        return productRepository.save(product);
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
     * @return The updated product.
     */
    @Transactional
    public Product updateProduct(Product product) {
        // Business Logic: Validate price integrity
        if (product.getDiscountedPrice() > product.getMrp()) {
            throw new IllegalArgumentException("Discounted price cannot be greater than MRP.");
        }

        // Check existence before update
        if (!productRepository.existsById(product.getProductId())) {
            throw new com.revshop.exception.ResourceNotFoundException(
                    "Product not found with ID: " + product.getProductId());
        }
        return productRepository.save(product);
    }

    public void deleteProduct(int productId, int sellerId) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new com.revshop.exception.ResourceNotFoundException(
                        "Product not found with ID: " + productId));

        if (p.getSellerId() != sellerId) {
            throw new IllegalArgumentException("Product does not belong to this seller.");
        }
        productRepository.deleteById(productId);
    }
}
