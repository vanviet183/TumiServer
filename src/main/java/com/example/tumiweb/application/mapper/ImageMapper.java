package com.example.tumiweb.application.mapper;

import com.example.tumiweb.domain.dto.ImageDTO;
import com.example.tumiweb.domain.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ImageMapper {

  @Mappings({
      @Mapping(target = "title", source = "imageDTO.title")
  })
  Image toImage(ImageDTO imageDTO);

}