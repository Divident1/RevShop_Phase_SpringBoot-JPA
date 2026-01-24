package com.revshop.service;

import com.revshop.dao.ProductDAO;
import com.revshop.model.Product;
import java.util.List;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();

    public boolean addProduct(Product product) {
        if (product.getDiscountedPrice() > product.getMrp()) {
            System.out.println("Error: Discounted price cannot be greater than MRP.");
            return false;
        }
        return productDAO.addProduct(product);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public List<Product> getProductsBySeller(int sellerId) {
        return productDAO.getProductsBySeller(sellerId);
    }

    public List<Product> searchProducts(String query) {
        return productDAO.searchProducts(query);
    }

    public List<Product> searchByCategory(int categoryId) {
        return productDAO.searchByCategory(categoryId);
    }

    public boolean updateProduct(Product product) {
        if (product.getDiscountedPrice() > product.getMrp()) {
            System.out.println("Error: Discounted price cannot be greater than MRP.");
            return false;
        }
        return productDAO.updateProduct(product);
    }

    public boolean deleteProduct(int productId, int sellerId) {
        return productDAO.deleteProduct(productId, sellerId);
    }
}
