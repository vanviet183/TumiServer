package com.example.tumiweb.services;

import com.example.tumiweb.dto.NotificationDTO;
import com.example.tumiweb.model.Notification;

import java.util.Set;

public interface INotificationService {
    Notification findNotificationById(Long id);
    Set<Notification> getAllNotification();
    Set<Notification> getAllNotificationWidthPage(Long page, int size);
    Notification getNotificationById(Long id);
    Notification createNotification(NotificationDTO notificationDTO);
    Notification editNotificationById(Long id, NotificationDTO notificationDTO);
    Notification deleteNotificationById(Long id);
    Notification changeStatusNotificationById(Long id);
}
