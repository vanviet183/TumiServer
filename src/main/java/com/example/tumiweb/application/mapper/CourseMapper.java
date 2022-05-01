package com.example.tumiweb.application.mapper;

import com.example.tumiweb.domain.dto.CourseDTO;
import com.example.tumiweb.domain.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CourseMapper {

  @Mappings({
      @Mapping(target = "name", source = "courseDTO.name"),
      @Mapping(target = "seo", source = "courseDTO.seo"),
      @Mapping(target = "price", source = "courseDTO.price"),
      @Mapping(target = "process", source = "courseDTO.process"),
      @Mapping(target = "description", source = "courseDTO.description")
  })
  Course toCourse(CourseDTO courseDTO);

}
