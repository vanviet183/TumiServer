package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dto.GiftOrderDTO;
import com.example.tumiweb.model.Gift;
import com.example.tumiweb.model.GiftOrder;
import com.example.tumiweb.model.User;
import com.example.tumiweb.services.IGiftOrderService;
import com.example.tumiweb.services.IGiftService;
import com.example.tumiweb.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/gift-order")
public class GiftOrderController extends BaseController<GiftOrder> {

    @Autowired
    private IGiftOrderService giftOrderService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IGiftService giftService;

    @GetMapping("")
    public ResponseEntity<?> getAllGiftOder(
            @RequestParam(name = "page", required = false) Long page,
            @RequestParam(name = "status", required = false) boolean status
    ) {
        return this.resSetSuccess(giftOrderService.getAllGiftOrder(page, Constants.SIZE_OFF_PAGE, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderGiftById(@PathVariable("id") Long id) {
        return this.resSuccess(giftOrderService.findGiftOrderById(id));
    }

    @PostMapping("/{userId}/{giftId}")
    public ResponseEntity<?> createNewGiftOrder(
            @PathVariable("userId") Long userId,
            @PathVariable("giftId") Long giftId,
            @RequestBody GiftOrderDTO giftOrderDTO
            ) {
        Gift gift = giftService.findGiftById(giftId);
        if(userService.changeMarkById(userId, gift.getMark())) {
            //điểm đã bị trừ ở trên if kia :v, đang lú nên note lại
            User user = userService.getUserById(userId);

            giftOrderDTO.setEmail(user.getEmail());
            giftOrderDTO.setQuality(1L);

            GiftOrder giftOrder = giftOrderService.createNewGiftOrder(giftOrderDTO, gift);

            Set<GiftOrder> giftOrders = user.getGiftOrders();
            giftOrders.add(giftOrder);

            Set<User> users =


            return this.resSuccess();
        }
        return this.resFailed();
    }
}
