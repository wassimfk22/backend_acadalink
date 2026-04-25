package com.acadlink.repository;

import com.acadlink.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDestinataireIdOrderByDateDesc(Long destinataireId);
    List<Notification> findByDestinataireIdAndLuFalse(Long destinataireId);
    long countByDestinataireIdAndLuFalse(Long destinataireId);
}
