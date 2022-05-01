package com.example.tumiweb.application.mapper;

import com.example.tumiweb.domain.dto.NotificationDTO;
import com.example.tumiweb.domain.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  @Mappings({
      @Mapping(target = "title", source = "notificationDTO.title"),
      @Mapping(target = "path", source = "notificationDTO.path")
  })
  Notification toNotification(NotificationDTO notificationDTO);

}