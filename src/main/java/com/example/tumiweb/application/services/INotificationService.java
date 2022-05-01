package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.dto.NotificationDTO;
import com.example.tumiweb.domain.entity.Notification;

import java.util.List;

public interface INotificationService {

  Notification findNotificationById(Long id);

  List<Notification> getAllNotification(Long page, int size, Boolean activeFlag);

  Notification createNotification(Long userId, NotificationDTO notificationDTO);

  Notification editNotificationById(Long id, NotificationDTO notificationDTO);

  Notification deleteNotificationById(Long id);

  Notification changeDeleteFlagNotificationById(Long id);

}
