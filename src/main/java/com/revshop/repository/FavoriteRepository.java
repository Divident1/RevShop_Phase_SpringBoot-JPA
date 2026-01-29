package com.revshop.repository;

import com.revshop.model.Favorite;
import com.revshop.model.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
    List<Favorite> findByUserId(int userId);

    boolean existsByUserIdAndProductId(int userId, int productId);

    void deleteByUserIdAndProductId(int userId, int productId);
}
