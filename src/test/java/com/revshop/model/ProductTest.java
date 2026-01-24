package com.revshop.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void testProductCreation() {
        Product product = new Product(1, 1, "Test Product", "Description", 100.0, 80.0, 50, 10);

        assertEquals(1, product.getSellerId());
        assertEquals(1, product.getCategoryId());
        assertEquals("Test Product", product.getName());
        assertEquals("Description", product.getDescription());
        assertEquals(100.0, product.getMrp());
        assertEquals(80.0, product.getDiscountedPrice());
        assertEquals(50, product.getQuantity());
        assertEquals(10, product.getThreshold());
    }

    @Test
    public void testProductWithId() {
        Product product = new Product(10, 1, 2, "Product", "Desc", 200.0, 150.0, 100, 20);

        assertEquals(10, product.getProductId());
        assertEquals(1, product.getSellerId());
        assertEquals(2, product.getCategoryId());
    }

    @Test
    public void testSettersAndGetters() {
        Product product = new Product();
        product.setProductId(1);
        product.setSellerId(2);
        product.setCategoryId(3);
        product.setName("Test");
        product.setDescription("Test Desc");
        product.setMrp(500.0);
        product.setDiscountedPrice(400.0);
        product.setQuantity(25);
        product.setThreshold(5);

        assertEquals(1, product.getProductId());
        assertEquals(2, product.getSellerId());
        assertEquals(3, product.getCategoryId());
        assertEquals("Test", product.getName());
        assertEquals("Test Desc", product.getDescription());
        assertEquals(500.0, product.getMrp());
        assertEquals(400.0, product.getDiscountedPrice());
        assertEquals(25, product.getQuantity());
        assertEquals(5, product.getThreshold());
    }
}
