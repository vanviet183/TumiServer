package com.example.tumiweb.application.mapper;

import com.example.tumiweb.domain.dto.UserDTO;
import com.example.tumiweb.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mappings({
      @Mapping(target = "id", source = "id"),
      @Mapping(target = "username", source = "userDTO.username"),
      @Mapping(target = "password", source = "userDTO.password"),
      @Mapping(target = "email", source = "userDTO.email"),
      @Mapping(target = "phone", source = "userDTO.phone"),
      @Mapping(target = "mark", source = "userDTO.mark"),
      @Mapping(target = "birthday", source = "userDTO.birthday"),
      @Mapping(target = "fullName", source = "userDTO.fullName")
  })
  User toUser(UserDTO userDTO, Long id);

}
