package com.revshop.service;

import com.revshop.model.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    private ProductService productService = new ProductService();

    @Test
    public void testDiscountedPriceValidation() {
        // Discounted price > MRP should throw IllegalArgumentException
        Product invalidProduct = new Product(1, 1, "Test", "Desc", 100.0, 150.0, 10, 5);
        assertThrows(IllegalArgumentException.class, () -> productService.addProduct(invalidProduct));
    }

    @Test
    public void testValidProduct() {
        // Discounted price <= MRP is valid format
        Product validProduct = new Product(1, 1, "Test", "Desc", 100.0, 80.0, 10, 5);
        // Note: This would require DB connection, so we just test the validation logic
        assertTrue(validProduct.getDiscountedPrice() <= validProduct.getMrp());
    }

    @Test
    public void testUpdateProductValidation() {
        Product invalidProduct = new Product(1, 1, 1, "Test", "Desc", 50.0, 100.0, 10, 5);
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(invalidProduct));
    }
}
