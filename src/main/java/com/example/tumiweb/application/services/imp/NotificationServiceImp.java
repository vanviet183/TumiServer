package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.constants.DevMessageConstant;
import com.example.tumiweb.application.constants.UserMessageConstant;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImp implements INotificationService {
  private final NotificationMapper notificationMapper = Mappers.getMapper(NotificationMapper.class);
  private final NotificationRepository notificationRepository;
  private final IUserService userService;

  public NotificationServiceImp(NotificationRepository notificationRepository, IUserService userService) {
    this.notificationRepository = notificationRepository;
    this.userService = userService;
  }

  @Override
  public Notification findNotificationById(Long id) {
    Optional<Notification> notification = notificationRepository.findById(id);
    if (notification.isEmpty()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "notification", id));
    }
    if (notification.get().getDeleteFlag()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.DATA_WAS_DELETE, id));
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
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "user", userId));
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