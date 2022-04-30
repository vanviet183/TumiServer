package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.dto.NotificationDTO;
import com.example.tumiweb.domain.entity.Notification;

import java.util.Set;

public interface INotificationService {

  Notification findNotificationById(Long id);

  Set<Notification> getAllNotification(Long page, int size, boolean status);

  Notification getNotificationById(Long id);

  Notification createNotification(Long userId, NotificationDTO notificationDTO);

  Notification editNotificationById(Long id, NotificationDTO notificationDTO);

  Notification deleteNotificationById(Long id);

  Notification changeStatusNotificationById(Long id);

}
