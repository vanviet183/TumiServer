package com.example.tumiweb.repository;

import com.example.tumiweb.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Set<Notification> findAllByStatus(boolean status);

}
