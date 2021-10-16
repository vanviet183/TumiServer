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

import java.util.*;

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
    public Set<Notification> getAllNotification(Long page, int size, boolean active) {
        List<Notification> notifications;
        if(page != null) {
            Page<Notification> notificationPage = notificationRepository.findAll(PageRequest.of(page.intValue(), size));
            notifications = notificationPage.getContent();
        }else {
            notifications = notificationRepository.findAll();
        }

        if(active) {
            if(page != null) {
                int length = notifications.size();
                int totalPage = (length % page != 0) ? length/size + 1 : length/size;
                if(totalPage > page.intValue()) {
                    return new HashSet<>();
                }
                notifications = notifications.subList(page.intValue()*size, page.intValue()*size + size);
            }else {
                notifications = new ArrayList<>(notificationRepository.findAllByStatus(true));
            }
        }

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
