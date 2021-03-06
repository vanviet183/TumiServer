package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  Set<Notification> findAllByDeleteFlag(boolean flag);

}
