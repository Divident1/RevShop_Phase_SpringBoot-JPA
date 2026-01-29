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
}
