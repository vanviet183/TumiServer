package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.NotificationDTO;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.dao.Gift;
import com.example.tumiweb.dao.GiftOrder;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.repository.GiftOrderRepository;
import com.example.tumiweb.services.IGiftOrderService;
import com.example.tumiweb.services.IGiftService;
import com.example.tumiweb.services.INotificationService;
import com.example.tumiweb.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class GiftOrderServiceImp implements IGiftOrderService {

    @Autowired private GiftOrderRepository giftOrderRepository;

    @Autowired private IUserService userService;

    @Autowired private IGiftService giftService;

    @Autowired private INotificationService notificationService;

    SimpleDateFormat formatDay = new SimpleDateFormat("dd/MM/yyyy");

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
    public GiftOrder createNewGiftOrder(Long userId, Long giftId) {
        Gift gift = giftService.findGiftById(giftId);
        if(userService.changeMarkById(userId, -gift.getMark())) {
            User user = userService.getUserById(userId);
            GiftOrder giftOrder = new GiftOrder();
            giftOrder.setEmail(user.getEmail());
            giftOrder.setQuality(1L);

            giftOrder = giftOrderRepository.save(giftOrder);
            user.addRelationGiftOrder(giftOrder);
            userService.save(user);

            //create notification
            NotificationDTO notificationDTO = new NotificationDTO("Bạn đã nhận được \'" + gift.getName() + "\'", "");
            notificationService.createNotification(userId, notificationDTO);

            return giftOrderRepository.save(giftOrder);
        }
        throw new NotFoundException("Can not create gift order");
    }

    @Override
    public GiftOrder changeStatusById(Long id) {
        GiftOrder giftOrder = findGiftOrderById(id);
        if(giftOrder == null) {
            throw new NotFoundException("Can not find GiftOrder by id: " + id);
        }
        giftOrder.setStatus(false);
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

    @Override
    public GiftOrder save(GiftOrder giftOrder) {
        return giftOrderRepository.save(giftOrder);
    }

    @Override
    public GiftOrder giveRandomGiftToUser(Long userId) {
        List<Gift> gifts = new ArrayList<>(giftService.getAllGift(null, 0, true));
        Random random = new Random();
//        int randomNumber = random.nextInt(max + 1 - min) + min;
        int index = random.nextInt(gifts.size());

//        createNewGiftOrder(userId, gifts.get(index).getId());
        User user = userService.getUserById(userId);
        GiftOrder giftOrder = new GiftOrder();
        giftOrder.setEmail(user.getEmail());
        giftOrder.setQuality(1L);
        giftOrder.setUser(user);
        Set<Gift> giftSet = giftOrder.getGifts();

        giftSet.add(gifts.get(index));
        giftOrder.setGifts(giftSet);

        GiftOrder newGiftOrder = giftOrderRepository.save(giftOrder);
        user.addRelationGiftOrder(newGiftOrder);

        Set<GiftOrder> giftOrders = gifts.get(index).getGiftOrders();
        giftOrders.add(newGiftOrder);
        gifts.get(index).setGiftOrders(giftOrders);

        giftService.save(gifts.get(index));

        userService.save(user);

        //create notification
        NotificationDTO notificationDTO = new NotificationDTO("Bạn đã nhận được \'" + gifts.get(index).getName() + "\'", "");
        notificationService.createNotification(userId, notificationDTO);

        return giftOrderRepository.save(newGiftOrder);
    }
}
