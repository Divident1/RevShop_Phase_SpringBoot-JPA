package com.revshop.repository;

import com.revshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findBySellerId(int sellerId);

    List<Product> findByCategoryId(int categoryId);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Custom JPQL Query (Stage 5 Requirement)
    @org.springframework.data.jpa.repository.Query("SELECT p FROM Product p WHERE p.discountedPrice < :maxPrice")
    List<Product> findProductsBelowPrice(@org.springframework.data.repository.query.Param("maxPrice") double maxPrice);
}
