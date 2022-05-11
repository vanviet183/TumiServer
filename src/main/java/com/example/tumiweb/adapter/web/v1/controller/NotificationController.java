package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.CommonConstant;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.INotificationService;
import com.example.tumiweb.domain.dto.NotificationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
public class NotificationController {
  private final INotificationService notificationService;

  public NotificationController(INotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @GetMapping(UrlConstant.Notification.DATA_NOTIFICATION_ID)
  public ResponseEntity<?> getNotificationById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(notificationService.findNotificationById(id));
  }

  @GetMapping(UrlConstant.Notification.DATA_NOTIFICATION)
  public ResponseEntity<?> getAllNotification(@RequestParam(name = "page", required = false) Long page,
                                              @RequestParam(name = "status", required = false) boolean status) {
    return VsResponseUtil.ok(notificationService.getAllNotification(page, CommonConstant.SIZE_OFF_PAGE, status));
  }

  @PostMapping(UrlConstant.Notification.DATA_NOTIFICATION_USER_ID)
  public ResponseEntity<?> createNewNotification(@PathVariable("userId") Long userId,
                                                 @RequestBody NotificationDTO notificationDTO) {
    return VsResponseUtil.ok(notificationService.createNotification(userId, notificationDTO));
  }

  @PatchMapping(UrlConstant.Notification.DATA_NOTIFICATION_ID)
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<?> editNotificationById(@PathVariable("id") Long id,
                                                @RequestBody NotificationDTO notificationDTO) {
    return VsResponseUtil.ok(notificationService.editNotificationById(id, notificationDTO));
  }

  @DeleteMapping(UrlConstant.Notification.DATA_NOTIFICATION_ID)
  public ResponseEntity<?> deleteNotificationById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(notificationService.deleteNotificationById(id));
  }

  @PostMapping(UrlConstant.Notification.DATA_NOTIFICATION_ID_STATUS)
  public ResponseEntity<?> changeStatusById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(notificationService.changeDeleteFlagNotificationById(id));
  }

}
