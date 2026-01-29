package com.revshop.repository;

import com.revshop.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserId(int userId);

    List<Notification> findByUserIdAndIsReadFalse(int userId);
}
