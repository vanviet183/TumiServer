package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.GiftOrderRepository;
import com.example.tumiweb.application.dai.GiftRepository;
import com.example.tumiweb.application.dai.NotificationRepository;
import com.example.tumiweb.application.dai.UserRepository;
import com.example.tumiweb.application.mapper.NotificationMapper;
import com.example.tumiweb.application.services.IGiftOrderService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.NotificationDTO;
import com.example.tumiweb.domain.entity.Gift;
import com.example.tumiweb.domain.entity.GiftOrder;
import com.example.tumiweb.domain.entity.Notification;
import com.example.tumiweb.domain.entity.User;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class GiftOrderServiceImp implements IGiftOrderService {
  private final NotificationMapper notificationMapper = Mappers.getMapper(NotificationMapper.class);
  private final GiftOrderRepository giftOrderRepository;
  private final UserRepository userRepository;
  private final GiftRepository giftRepository;
  private final NotificationRepository notificationRepository;

  public GiftOrderServiceImp(GiftOrderRepository giftOrderRepository, UserRepository userRepository,
                             GiftRepository giftRepository, NotificationRepository notificationRepository) {
    this.giftOrderRepository = giftOrderRepository;
    this.userRepository = userRepository;
    this.giftRepository = giftRepository;
    this.notificationRepository = notificationRepository;
  }

  //  @Cacheable(value = "gift-order", key = "'all'")
  @Override
  public Set<GiftOrder> getAllGiftOrder(Long page, int size, boolean active) {
    List<GiftOrder> giftOrders;
    if (page != null) {
      Page<GiftOrder> giftOrderPage = giftOrderRepository.findAll(PageRequest.of(page.intValue(), size));
      giftOrders = giftOrderPage.getContent();
    } else {
      giftOrders = giftOrderRepository.findAll();
    }

    if (active) {
      giftOrders = new ArrayList<>(giftOrderRepository.findAllByDeleteFlag(true));
      if (page != null) {
        int length = giftOrders.size();
        int totalPage = (length % page != 0) ? length / size + 1 : length / size;
        if (totalPage > page.intValue()) {
          return new HashSet<>();
        }
        giftOrders = giftOrders.subList(page.intValue() * size, page.intValue() * size + size);
      }
    }

    return new HashSet<>(giftOrders);
  }

  //  @CacheEvict(value = "gift-order", allEntries = true)
  @Override
  public GiftOrder createNewGiftOrder(Long userId, Long giftId) {
    Gift gift = findGiftById(giftId);
    User user = getUserById(userId);
    user.setMark(user.getMark() - gift.getMark());
    if (user.getMark() >= 0) {
      GiftOrder giftOrder = new GiftOrder();
      giftOrder.setEmail(user.getEmail());
      giftOrder.setQuality(1L);

      giftOrder = giftOrderRepository.save(giftOrder);
      userRepository.save(user);

      //create notification
      NotificationDTO notificationDTO = new NotificationDTO("Bạn đã nhận được \'" + gift.getName() + "\'", "");
      Notification notification = notificationMapper.toNotification(notificationDTO);
      notification.setUser(user);
      notificationRepository.save(notification);

      return giftOrderRepository.save(giftOrder);
    }
    throw new VsException("Can not create gift order");
  }

  //  @CacheEvict(value = "gift-order", allEntries = true)
  @Override
  public GiftOrder changeStatusById(Long id) {
    GiftOrder giftOrder = findGiftOrderById(id);

    giftOrder.setDeleteFlag(!giftOrder.getDeleteFlag());

    return giftOrderRepository.save(giftOrder);
  }

  //  @CacheEvict(value = "gift-order", allEntries = true)
  @Override
  public GiftOrder deleteGiftOrderById(Long id) {
    GiftOrder giftOrder = findGiftOrderById(id);

    giftOrderRepository.delete(giftOrder);

    return giftOrder;
  }

  //  @Cacheable(value = "gift-order", key = "#id")
  @Override
  public GiftOrder findGiftOrderById(Long id) {
    Optional<GiftOrder> giftOrder = giftOrderRepository.findById(id);
    if (giftOrder.isEmpty()) {
      throw new VsException("Can not find giftOrder by id: " + id);
    }
    if (giftOrder.get().getDeleteFlag()) {
      throw new VsException("This giftOrder was delete");
    }
    return giftOrder.get();
  }

  @Override
  public GiftOrder save(GiftOrder giftOrder) {
    return giftOrderRepository.save(giftOrder);
  }

  //  @CacheEvict(value = "gift-order", allEntries = true)
  @Override
  public GiftOrder giveRandomGiftToUser(Long userId) {
    List<Gift> gifts = giftRepository.findAllByDeleteFlag(false);
    Random random = new Random();
    int index = random.nextInt(gifts.size());

    User user = getUserById(userId);
    GiftOrder giftOrder = new GiftOrder();
    giftOrder.setEmail(user.getEmail());
    giftOrder.setQuality(1L);
    giftOrder.setUser(user);
    Set<Gift> giftSet = giftOrder.getGifts();

    giftSet.add(gifts.get(index));
    giftOrder.setGifts(giftSet);

    GiftOrder newGiftOrder = giftOrderRepository.save(giftOrder);

    Set<GiftOrder> giftOrders = gifts.get(index).getGiftOrders();
    giftOrders.add(newGiftOrder);
    gifts.get(index).setGiftOrders(giftOrders);

    giftRepository.save(gifts.get(index));

    userRepository.save(user);

    //create notification
    NotificationDTO notificationDTO = new NotificationDTO("Bạn đã nhận được \'" + gifts.get(index).getName() + "\'",
        "");
    Notification notification = notificationMapper.toNotification(notificationDTO);
    notification.setUser(user);
    notificationRepository.save(notification);

    return giftOrderRepository.save(newGiftOrder);
  }

  private Gift findGiftById(Long id) {
    Optional<Gift> gift = giftRepository.findById(id);
    if (gift.isEmpty()) {
      throw new VsException("Can not find gift by id: " + id);
    }
    if (gift.get().getDeleteFlag()) {
      throw new VsException("This gift was delete");
    }
    if (!gift.get().getActiveFlag()) {
      throw new VsException("This gift was disable");
    }

    return gift.get();
  }

  private User getUserById(Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isEmpty()) {
      throw new VsException("Can not find user by id: " + id);
    }
    if (user.get().getDeleteFlag()) {
      throw new VsException("This user was delete");
    }
    if (!user.get().getActiveFlag()) {
      throw new VsException("This user was disable");
    }
    return user.get();
  }

}
