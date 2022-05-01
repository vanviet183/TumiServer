package com.example.tumiweb.application.mapper;

import com.example.tumiweb.domain.dto.ChapterDTO;
import com.example.tumiweb.domain.entity.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

  @Mappings({
      @Mapping(target = "name", source = "chapterDTO.name"),
      @Mapping(target = "seo", source = "chapterDTO.seo")
  })
  Chapter toChapter(ChapterDTO chapterDTO);

}