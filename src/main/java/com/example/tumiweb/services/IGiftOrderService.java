package com.example.tumiweb.services;

import com.example.tumiweb.dto.GiftOrderDTO;
import com.example.tumiweb.model.Gift;
import com.example.tumiweb.model.GiftOrder;

import java.util.Set;

public interface IGiftOrderService {
    Set<GiftOrder> getAllGiftOrder(Long page, int size, boolean active);
    GiftOrder createNewGiftOrder(GiftOrderDTO giftOrderDTO, Gift gifts);
    GiftOrder changeStatusById(Long id);
    GiftOrder deleteGiftOrderById(Long id);
    GiftOrder findGiftOrderById(Long id);
}
