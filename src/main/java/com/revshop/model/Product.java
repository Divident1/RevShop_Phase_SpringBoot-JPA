package com.revshop.model;

public class Product {
    private int productId;
    private int sellerId;
    private int categoryId;
    private String name;
    private String description;
    private double mrp;
    private double discountedPrice;
    private int quantity;
    private int threshold;

    public Product() {
    }

    public Product(int productId, int sellerId, int categoryId, String name, String description, double mrp,
            double discountedPrice, int quantity, int threshold) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.mrp = mrp;
        this.discountedPrice = discountedPrice;
        this.quantity = quantity;
        this.threshold = threshold;
    }

    // Constructor for creating new product (without ID)
    public Product(int sellerId, int categoryId, String name, String description, double mrp, double discountedPrice,
            int quantity, int threshold) {
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.mrp = mrp;
        this.discountedPrice = discountedPrice;
        this.quantity = quantity;
        this.threshold = threshold;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Price: %.2f | Stock: %d", productId, name, discountedPrice, quantity);
    }
}
