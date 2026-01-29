package com.revshop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Favorites")
@IdClass(FavoriteId.class)
public class Favorite {
    @Id
    private int userId;

    @Id
    private int productId;

    public Favorite() {
    }

    public Favorite(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
