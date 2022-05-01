package com.example.tumiweb.application.mapper;

import com.example.tumiweb.domain.dto.HelpDTO;
import com.example.tumiweb.domain.entity.Help;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface HelpMapper {

  @Mappings({
      @Mapping(target = "title", source = "helpDTO.title")
  })
  Help toHelp(HelpDTO helpDTO);

}
