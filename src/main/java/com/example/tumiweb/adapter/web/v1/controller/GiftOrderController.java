package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.EmailConstant;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.IGiftOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
public class GiftOrderController {
  private final IGiftOrderService giftOrderService;

  public GiftOrderController(IGiftOrderService giftOrderService) {
    this.giftOrderService = giftOrderService;
  }

  @GetMapping(UrlConstant.GiftOrder.DATA_GIFT_ORDER)
  public ResponseEntity<?> getAllGiftOder(@RequestParam(name = "page", required = false) Long page,
                                          @RequestParam(name = "status", required = false) boolean status) {
    return VsResponseUtil.ok(giftOrderService.getAllGiftOrder(page, EmailConstant.SIZE_OFF_PAGE, status));
  }

  @GetMapping(UrlConstant.GiftOrder.DATA_GIFT_ORDER_ID)
  public ResponseEntity<?> getOrderGiftById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(giftOrderService.findGiftOrderById(id));
  }

  @PostMapping(UrlConstant.GiftOrder.DATA_GIFT_ORDER_CREATE)
  public ResponseEntity<?> createNewGiftOrder(@PathVariable("userId") Long userId,
                                              @PathVariable("giftId") Long giftId) {
    return VsResponseUtil.ok(giftOrderService.createNewGiftOrder(userId, giftId));
  }

  @PostMapping(UrlConstant.GiftOrder.DATA_GIFT_ORDER_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> disableStatusOrderGift(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(giftOrderService.changeStatusById(id));
  }

  @DeleteMapping(UrlConstant.GiftOrder.DATA_GIFT_ORDER)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> deleteGiftOrder(@RequestParam("id") Long id) {
    return VsResponseUtil.ok(giftOrderService.deleteGiftOrderById(id));
  }

}
