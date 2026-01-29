package com.revshop.controller;

import com.revshop.exception.ResourceNotFoundException;
import com.revshop.model.Product;
import com.revshop.service.ProductService;
import com.revshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        // Business logic exception triggering
        return productRepository.findById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        boolean isCreated = productService.addProduct(product);
        if (isCreated) {
            return new ResponseEntity<>("Product created successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to create product. Check data integrity.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestBody Product product) {
        product.setProductId(id);
        boolean isUpdated = productService.updateProduct(product);
        if (isUpdated) {
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Product not found with id: " + id + " or invalid data");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        // For simple demo, passing 0 as sellerId or assuming admin override logic if we
        // had one
        // Ideally we check permissions.
        // For now, let's just assume we want to delete by ID regardless of seller for
        // API admin usage
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
    }
}
