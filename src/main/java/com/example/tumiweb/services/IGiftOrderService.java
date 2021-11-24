package com.example.tumiweb.services;

import com.example.tumiweb.dao.GiftOrder;

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
