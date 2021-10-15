package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.NotificationDTO;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.model.Notification;
import com.example.tumiweb.repository.NotificationRepository;
import com.example.tumiweb.services.INotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class NotificationServiceImp implements INotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Notification findNotificationById(Long id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        if(notification.isEmpty()) {
            return null;
        }
        return notification.get();
    }

    @Override
    public Set<Notification> getAllNotification() {
        List<Notification> notifications = notificationRepository.findAll();
        if(notifications.isEmpty()) {
            throw new NotFoundException("Notification list is empty");
        }
        return new HashSet<>(notifications);
    }

    @Override
    public Set<Notification> getAllNotificationWidthPage(Long page, int size) {
        Page<Notification> notificationPage = notificationRepository.findAll(PageRequest.of(page.intValue(), size));
        List<Notification> notifications = notificationPage.getContent();
        if(notifications.isEmpty()) {
            throw new NotFoundException("Notification list is empty");
        }
        return new HashSet<>(notifications);
    }

    @Override
    public Notification getNotificationById(Long id) {
        Notification notification = findNotificationById(id);
        if(notification == null) {
            throw new NotFoundException("Can not find notification with id: " + id);
        }
        return notification;
    }

    @Override
    public Notification createNotification(NotificationDTO notificationDTO) {
        return notificationRepository.save(modelMapper.map(notificationDTO, Notification.class));
    }

    @Override
    public Notification editNotificationById(Long id, NotificationDTO notificationDTO) {
        Notification notification = findNotificationById(id);
        if(notification == null) {
            throw new NotFoundException("Can not find notification with id: " + id);
        }
        Notification newNotification = modelMapper.map(notificationDTO, Notification.class);
        newNotification.setId(notification.getId());
        return notificationRepository.save(newNotification);
    }

    @Override
    public Notification deleteNotificationById(Long id) {
        Notification notification = findNotificationById(id);
        if(notification == null) {
            throw new NotFoundException("Can not find notification with id: " + id);
        }
        notificationRepository.delete(notification);
        return notification;
    }

    @Override
    public Notification changeStatusNotificationById(Long id) {
        Notification notification = findNotificationById(id);
        if(notification == null) {
            throw new NotFoundException("Can not find notification with id: " + id);
        }
        if(notification.getStatus()) {
            notification.setStatus(false);
        }
        return notificationRepository.save(notification);
    }
}
