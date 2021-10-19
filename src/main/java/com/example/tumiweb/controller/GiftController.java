package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dto.GiftDTO;
import com.example.tumiweb.model.Gift;
import com.example.tumiweb.services.IGiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/gifts")
public class GiftController extends BaseController<Gift> {

    @Autowired
    private IGiftService giftService;

    @GetMapping("")
    public ResponseEntity<?> getAllGifts(
        @RequestParam(name = "page", required = false) Long page,
        @RequestParam(name = "status", required = false) boolean status,
        @RequestParam(name = "both", required = false) boolean bool
    ) {
        return this.resSetSuccess(giftService.getAllGift(page, Constants.SIZE_OFF_PAGE, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGiftById(@PathVariable("id") Long id) {
        return this.resSuccess(giftService.findGiftById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> createNewGift(
            @RequestParam(name = "image", required = false) MultipartFile image,
            @RequestBody GiftDTO giftDTO) {
        return this.resSuccess(giftService.createNewGift(giftDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editGiftById(
            @PathVariable("id") Long id,
            @RequestParam(name = "image", required = false) MultipartFile image,
            @RequestBody GiftDTO giftDTO
    ) {
        return this.resSuccess(giftService.editGiftById(id, giftDTO, image));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Long id) {
        return this.resSuccess(giftService.changeStatusById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGiftById(@PathVariable("id") Long id) {
        return this.resSuccess(giftService.deleteGiftById(id));
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<?> changeImageById(
            @PathVariable("id") Long id,
            @RequestParam(name = "image", required = false) MultipartFile multipartFile
    ) {
        return this.resSuccess(giftService.changeImageGiftById(id, multipartFile));
    }


}
