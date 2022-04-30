package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.NotificationRepository;
import com.example.tumiweb.application.services.INotificationService;
import com.example.tumiweb.application.services.IUserService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.NotificationDTO;
import com.example.tumiweb.domain.entity.Notification;
import com.example.tumiweb.domain.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationServiceImp implements INotificationService {
  @Autowired
  private NotificationRepository notificationRepository;
  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private IUserService userService;

  @Override
  public Notification findNotificationById(Long id) {
    Optional<Notification> notification = notificationRepository.findById(id);
    if (notification.isEmpty()) {
      return null;
    }
    return notification.get();
  }

  //  @Cacheable(value = "notification", key = "'all'")
  @Override
  public Set<Notification> getAllNotification(Long page, int size, boolean status) {
    List<Notification> notifications;
    if (page != null) {
      Page<Notification> notificationPage = notificationRepository.findAll(PageRequest.of(page.intValue(), size));
      notifications = notificationPage.getContent();
    } else {
      notifications = notificationRepository.findAll();
    }

    if (status) {
      if (page != null) {
        int length = notifications.size();
        int totalPage = (length % page != 0) ? length / size + 1 : length / size;
        if (totalPage > page.intValue()) {
          return new HashSet<>();
        }
        notifications = notifications.subList(page.intValue() * size, page.intValue() * size + size);
      } else {
        notifications = new ArrayList<>(notificationRepository.findAllByDeleteFlag(true));
      }
    }

    return new HashSet<>(notifications);
  }

  //  @Cacheable(value = "notification", key = "#id")
  @Override
  public Notification getNotificationById(Long id) {
    Notification notification = findNotificationById(id);
    if (notification == null) {
      throw new VsException("Can not find notification with id: " + id);
    }
    return notification;
  }

  //  @CacheEvict(value = "notification", allEntries = true)
  @Override
  public Notification createNotification(Long userId, NotificationDTO notificationDTO) {
    User user = userService.getUserById(userId);
    if (user == null) {
      throw new VsException("Can not find user by id: " + userId);
    }
    Notification notification = modelMapper.map(notificationDTO, Notification.class);
    notification.setUser(user);

    return notificationRepository.save(notification);
  }

  //  @CacheEvict(value = "notification", allEntries = true)
  @Override
  public Notification editNotificationById(Long id, NotificationDTO notificationDTO) {
    Notification notification = findNotificationById(id);
    if (notification == null) {
      throw new VsException("Can not find notification with id: " + id);
    }
    Notification newNotification = modelMapper.map(notificationDTO, Notification.class);
    newNotification.setId(notification.getId());
    return notificationRepository.save(newNotification);
  }

  //  @CacheEvict(value = "notification", allEntries = true)
  @Override
  public Notification deleteNotificationById(Long id) {
    Notification notification = findNotificationById(id);
    if (notification == null) {
      throw new VsException("Can not find notification with id: " + id);
    }
    notificationRepository.delete(notification);
    return notification;
  }

  //  @CacheEvict(value = "notification", allEntries = true)
  @Override
  public Notification changeStatusNotificationById(Long id) {
    Notification notification = findNotificationById(id);
    if (notification == null) {
      throw new VsException("Can not find notification with id: " + id);
    }
    return notificationRepository.save(notification);
  }

}
