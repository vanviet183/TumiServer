package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dto.NotificationDTO;
import com.example.tumiweb.model.Notification;
import com.example.tumiweb.services.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notif")
public class NotificationController extends BaseController<Notification> {

    @Autowired
    private INotificationService notificationService;



    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable("id") Long id) {
        return this.resSuccess(notificationService.getNotificationById(id));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllNotification(
            @RequestParam(name = "page", required = false) Long page,
            @RequestParam(name = "status", required = false) boolean status
    ) {
        return this.resSetSuccess(notificationService.getAllNotification(page, Constants.SIZE_OFF_PAGE, status));
    }

    @PostMapping("")
    public ResponseEntity<?> createNewNotification(@RequestBody NotificationDTO notificationDTO) {
        return this.resSuccess(notificationService.createNotification(notificationDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editNotificationById(
            @PathVariable("id") Long id,
            @RequestBody NotificationDTO notificationDTO
    ) {
        return this.resSuccess(notificationService.editNotificationById(id, notificationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotificationById(@PathVariable("id") Long id) {
        return this.resSuccess(notificationService.deleteNotificationById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatusById(@PathVariable("id") Long id) {
        return this.resSuccess(notificationService.changeStatusNotificationById(id));
    }

}
