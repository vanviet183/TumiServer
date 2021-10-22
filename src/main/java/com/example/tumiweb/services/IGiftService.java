package com.example.tumiweb.services;

import com.example.tumiweb.dto.GiftDTO;
import com.example.tumiweb.dao.Gift;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface IGiftService {
    Set<Gift> getAllGift(Long page, int size, boolean active);
    Gift findGiftById(Long id);
    Gift createNewGift(GiftDTO giftDTO);
    Gift editGiftById(Long id, GiftDTO giftDTO, MultipartFile multipartFile);
    Gift changeStatusById(Long id);
    Gift deleteGiftById(Long id);
    Gift changeImageGiftById(Long id, MultipartFile multipartFile);

    //chưa paging
//    Set<Gift> findAllGiftByUserId(Long userId, boolean active, boolean both);
}
