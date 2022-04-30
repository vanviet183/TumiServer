package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.dto.GiftDTO;
import com.example.tumiweb.domain.entity.Gift;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface IGiftService {

  Set<Gift> getAllGift(Long page, int size, boolean active);

  Gift findGiftById(Long id);

  Gift createNewGift(GiftDTO giftDTO, MultipartFile file);

  Gift editGiftById(Long id, GiftDTO giftDTO, MultipartFile multipartFile);

  Gift changeStatusById(Long id);

  Gift deleteGiftById(Long id);

  Gift changeImageGiftById(Long id, MultipartFile multipartFile);

  List<Gift> getGiftsByKey(String key);

  Gift save(Gift gift);
  //chưa paging
//    Set<Gift> findAllGiftByUserId(Long userId, boolean active, boolean both);

}
