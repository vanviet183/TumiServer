package com.example.tumiweb.application.mapper;

import com.example.tumiweb.domain.dto.QuestionDTO;
import com.example.tumiweb.domain.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

  @Mappings({
      @Mapping(target = "title", source = "questionDTO.title"),
      @Mapping(target = "seo", source = "questionDTO.seo"),
      @Mapping(target = "avatar", source = "questionDTO.avatar")
  })
  Question toQuestion(QuestionDTO questionDTO);

}
