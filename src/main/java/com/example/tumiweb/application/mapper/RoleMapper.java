package com.example.tumiweb.application.mapper;

import com.example.tumiweb.domain.dto.RoleDTO;
import com.example.tumiweb.domain.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RoleMapper {

  @Mappings({
      @Mapping(target = "name", source = "roleDTO.name"),
      @Mapping(target = "description", source = "roleDTO.description")
  })
  Role toRole(RoleDTO roleDTO);

}