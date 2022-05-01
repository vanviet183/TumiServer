package com.example.tumiweb.application.mapper;

import com.example.tumiweb.domain.dto.CategoryDTO;
import com.example.tumiweb.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  @Mappings({
      @Mapping(target = "name", source = "categoryDTO.name"),
      @Mapping(target = "description", source = "categoryDTO.description")
  })
  Category toCategory(CategoryDTO categoryDTO);

}
