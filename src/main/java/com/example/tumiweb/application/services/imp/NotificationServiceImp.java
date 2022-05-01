package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.NotificationRepository;
import com.example.tumiweb.application.mapper.NotificationMapper;
import com.example.tumiweb.application.services.INotificationService;
import com.example.tumiweb.application.services.IUserService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.NotificationDTO;
import com.example.tumiweb.domain.entity.Notification;
import com.example.tumiweb.domain.entity.User;
import com.example.tumiweb.domain.entity.base.AbstractAuditingEntity;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImp implements INotificationService {
  private final NotificationMapper notificationMapper = Mappers.getMapper(NotificationMapper.class);
  @Autowired
  private NotificationRepository notificationRepository;
  @Autowired
  private IUserService userService;

  @Override
  public Notification findNotificationById(Long id) {
    Optional<Notification> notification = notificationRepository.findById(id);
    if (notification.isEmpty()) {
      throw new VsException("Can not find notification by id: " + id);
    }
    if (notification.get().getDeleteFlag()) {
      throw new VsException("This notification was delete");
    }
    return notification.get();
  }

  //  @Cacheable(value = "notification", key = "'all'")
  @Override
  public List<Notification> getAllNotification(Long page, int size, Boolean activeFlag) {
    List<Notification> notifications;
    if (page != null) {
      notifications = notificationRepository.findAll(PageRequest.of(page.intValue(), size)).getContent();
    } else {
      notifications = notificationRepository.findAll();
    }

    if (activeFlag) {
      return notifications.parallelStream().filter(AbstractAuditingEntity::getActiveFlag).collect(Collectors.toList());
    }

    return notifications;
  }

  //  @CacheEvict(value = "notification", allEntries = true)
  @Override
  public Notification createNotification(Long userId, NotificationDTO notificationDTO) {
    User user = userService.getUserById(userId);
    if (user == null) {
      throw new VsException("Can not find user by id: " + userId);
    }
    Notification notification = notificationMapper.toNotification(notificationDTO);
    notification.setUser(user);

    return notificationRepository.save(notification);
  }

  //  @CacheEvict(value = "notification", allEntries = true)
  @Override
  public Notification editNotificationById(Long id, NotificationDTO notificationDTO) {
    Notification notification = findNotificationById(id);

    Notification newNotification = notificationMapper.toNotification(notificationDTO);
    newNotification.setId(notification.getId());

    return notificationRepository.save(newNotification);
  }

  //  @CacheEvict(value = "notification", allEntries = true)
  @Override
  public Notification deleteNotificationById(Long id) {
    Notification notification = findNotificationById(id);

    notification.setDeleteFlag(true);

    return notificationRepository.save(notification);
  }

  //  @CacheEvict(value = "notification", allEntries = true)
  @Override
  public Notification changeDeleteFlagNotificationById(Long id) {
    Notification notification = findNotificationById(id);
    notification.setDeleteFlag(!notification.getDeleteFlag());
    return notificationRepository.save(notification);
  }

}
