package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.GiftOrderDTO;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.model.Gift;
import com.example.tumiweb.model.GiftOrder;
import com.example.tumiweb.repository.GiftOrderRepository;
import com.example.tumiweb.services.IGiftOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GiftOrderServiceImp implements IGiftOrderService {

    @Autowired
    private GiftOrderRepository giftOrderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Set<GiftOrder> getAllGiftOrder(Long page, int size, boolean active) {
        List<GiftOrder> giftOrders;
        if(page != null) {
            Page<GiftOrder> giftOrderPage = giftOrderRepository.findAll(PageRequest.of(page.intValue(), size));
            giftOrders = giftOrderPage.getContent();
        }else {
            giftOrders = giftOrderRepository.findAll();
        }

        if(active) {
            giftOrders = new ArrayList<>(giftOrderRepository.findAllByStatus(true));
            if(page != null) {
                int length = giftOrders.size();
                int totalPage = (length % page != 0) ? length/size + 1 : length/size;
                if(totalPage > page.intValue()) {
                    return new HashSet<>();
                }
                giftOrders = giftOrders.subList(page.intValue()*size, page.intValue()*size + size);
            }
        }

        return new HashSet<>(giftOrders);
    }

    @Override
    public GiftOrder createNewGiftOrder(GiftOrderDTO giftOrderDTO, Set<Gift> gifts) {
        if(gifts.isEmpty()) {
            throw new NotFoundException("Gift list is empty, can not create gift order");
        }
        giftOrderDTO.setGifts(gifts);
        return giftOrderRepository.save(modelMapper.map(giftOrderDTO, GiftOrder.class));
    }

    @Override
    public GiftOrder changeStatusById(Long id) {
        GiftOrder giftOrder = findGiftOrderById(id);
        if(giftOrder == null) {
            throw new NotFoundException("Can not find GiftOrder by id: " + id);
        }
        giftOrder.setStatus(!giftOrder.getStatus());
        return giftOrderRepository.save(giftOrder);
    }

    @Override
    public GiftOrder deleteGiftOrderById(Long id) {
        GiftOrder giftOrder = findGiftOrderById(id);
        if(giftOrder == null) {
            throw new NotFoundException("Can not find GiftOrder by id: " + id);
        }
        giftOrderRepository.delete(giftOrder);
        return giftOrder;
    }

    @Override
    public GiftOrder findGiftOrderById(Long id) {
        Optional<GiftOrder> giftOrder = giftOrderRepository.findById(id);
        if(giftOrder.isEmpty()) {
            return null;
        }
        return giftOrder.get();
    }
}
