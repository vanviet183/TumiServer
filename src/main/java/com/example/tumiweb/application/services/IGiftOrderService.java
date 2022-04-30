package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.entity.GiftOrder;

import java.util.Set;

public interface IGiftOrderService {

  Set<GiftOrder> getAllGiftOrder(Long page, int size, boolean active);

  GiftOrder createNewGiftOrder(Long userId, Long giftId);

  GiftOrder changeStatusById(Long id);

  GiftOrder deleteGiftOrderById(Long id);

  GiftOrder findGiftOrderById(Long id);

  GiftOrder save(GiftOrder giftOrder);

  GiftOrder giveRandomGiftToUser(Long userId);

}
