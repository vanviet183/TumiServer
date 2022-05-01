package com.example.tumiweb.application.mapper;

import com.example.tumiweb.domain.dto.AnswerDTO;
import com.example.tumiweb.domain.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

  @Mappings({
      @Mapping(target = "title", source = "answerDTO.title"),
      @Mapping(target = "image", source = "answerDTO.image"),
      @Mapping(target = "isTrue", source = "answerDTO.isTrue")
  })
  Answer toAnswer(AnswerDTO answerDTO);

}
