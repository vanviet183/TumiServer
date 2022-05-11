package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.CommonConstant;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.IGiftService;
import com.example.tumiweb.domain.dto.GiftDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestApiV1
public class GiftController {
  private final IGiftService giftService;

  public GiftController(IGiftService giftService) {
    this.giftService = giftService;
  }

  @GetMapping(UrlConstant.Gift.DATA_GIFT)
  public ResponseEntity<?> getAllGifts(@RequestParam(name = "page", required = false) Long page,
                                       @RequestParam(name = "status", required = false) boolean status,
                                       @RequestParam(name = "both", required = false) boolean bool) {
    return VsResponseUtil.ok(giftService.getAllGift(page, CommonConstant.SIZE_OFF_PAGE, status));
  }

  @GetMapping(UrlConstant.Gift.DATA_GIFT_ID)
  public ResponseEntity<?> getGiftById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(giftService.findGiftById(id));
  }

  @PostMapping(UrlConstant.Gift.DATA_GIFT)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> createNewGift(@RequestParam(name = "image", required = false) MultipartFile image,
                                         @RequestBody GiftDTO giftDTO) {
    return VsResponseUtil.ok(giftService.createNewGift(giftDTO, image));
  }

  @PatchMapping(UrlConstant.Gift.DATA_GIFT_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> editGiftById(@PathVariable("id") Long id,
                                        @RequestParam(name = "image", required = false) MultipartFile image,
                                        @RequestBody GiftDTO giftDTO) {
    return VsResponseUtil.ok(giftService.editGiftById(id, giftDTO, image));
  }

  @PostMapping(UrlConstant.Gift.DATA_GIFT_ID_STATUS)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> changeStatus(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(giftService.changeStatusById(id));
  }

  @DeleteMapping(UrlConstant.Gift.DATA_GIFT_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> deleteGiftById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(giftService.deleteGiftById(id));
  }

  @PostMapping(UrlConstant.Gift.DATA_GIFT_ID_IMAGE)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> changeImageById(@PathVariable("id") Long id,
                                           @RequestParam(name = "image", required = false) MultipartFile multipartFile) {
    return VsResponseUtil.ok(giftService.changeImageGiftById(id, multipartFile));
  }

  @GetMapping(UrlConstant.Gift.DATA_GIFT_SEARCH)
  public ResponseEntity<?> getGiftsByKey(@RequestParam("q") String key) {
    return VsResponseUtil.ok(giftService.getGiftsByKey(key));
  }

}
