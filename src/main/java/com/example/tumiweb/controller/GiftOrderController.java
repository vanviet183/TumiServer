package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dao.GiftOrder;
import com.example.tumiweb.services.IGiftOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/gift-order")
public class GiftOrderController extends BaseController<GiftOrder> {

    @Autowired
    private IGiftOrderService giftOrderService;

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
            @PathVariable("giftId") Long giftId
            ) {
        return this.resSuccess(giftOrderService.createNewGiftOrder(userId, giftId));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> disableStatusOrderGift(@PathVariable("id") Long id) {
        return this.resSuccess(giftOrderService.changeStatusById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGiftOrder(@PathVariable("id") Long id) {
        return this.resSuccess(giftOrderService.deleteGiftOrderById(id));
    }
}
