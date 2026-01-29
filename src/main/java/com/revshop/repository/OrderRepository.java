package com.revshop.repository;

import com.revshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByBuyerId(int buyerId);

    @Query(value = "SELECT DISTINCT o.* FROM Orders o JOIN OrderItems oi ON o.orderId = oi.orderId JOIN Products p ON oi.productId = p.productId WHERE p.sellerId = :sellerId", nativeQuery = true)
    List<Order> findOrdersBySellerId(int sellerId);
}
