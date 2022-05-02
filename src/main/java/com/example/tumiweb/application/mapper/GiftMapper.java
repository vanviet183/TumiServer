package com.example.tumiweb.application.mapper;

import com.example.tumiweb.domain.dto.GiftDTO;
import com.example.tumiweb.domain.entity.Gift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface GiftMapper {

  @Mappings({
      @Mapping(target = "id", source = "id"),
      @Mapping(target = "name", source = "giftDTO.name"),
      @Mapping(target = "avatar", source = "giftDTO.avatar"),
      @Mapping(target = "mark", source = "giftDTO.mark"),
  })
  Gift toGift(GiftDTO giftDTO, Long id);

}
