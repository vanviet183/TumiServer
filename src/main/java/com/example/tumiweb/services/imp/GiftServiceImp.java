package com.example.tumiweb.services.imp;

import com.example.tumiweb.base.BaseEntity;
import com.example.tumiweb.dto.GiftDTO;
import com.example.tumiweb.exception.DuplicateException;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.dao.Gift;
import com.example.tumiweb.repository.GiftRepository;
import com.example.tumiweb.services.IGiftService;
import com.example.tumiweb.utils.ConvertObject;
import com.example.tumiweb.utils.UploadFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiftServiceImp implements IGiftService {

    @Autowired
    private GiftRepository giftRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UploadFile uploadFile;

    @Override
    public Set<Gift> getAllGift(Long page, int size, boolean active) {
        List<Gift> gifts;
        if(page != null) {
            Page<Gift> giftPage = giftRepository.findAll(PageRequest.of(page.intValue(), size));
            gifts = giftPage.getContent();
        }else {
            gifts = giftRepository.findAll();
        }

        if(active) {
            gifts = gifts.stream().filter(BaseEntity::getStatus).collect(Collectors.toList());
        }

        return new HashSet<>(gifts);
    }

    @Override
    public Gift findGiftById(Long id) {
        Optional<Gift> gift = giftRepository.findById(id);
        if(gift.isEmpty()) {
            return null;
        }
        return gift.get();
    }

    @Override
    public Gift createNewGift(GiftDTO giftDTO, MultipartFile file) {
        if(giftRepository.findByName(giftDTO.getName()) != null) {
            throw new DuplicateException("Duplicate gift with title: " + giftDTO.getName());
        }
        Gift gift = modelMapper.map(giftDTO, Gift.class);
        gift.setAvatar(uploadFile.getUrlFromFile(file));

        return giftRepository.save(gift);
    }

    @Override
    public Gift editGiftById(Long id, GiftDTO giftDTO, MultipartFile avatar) {
        Gift gift = findGiftById(id);
        if(gift == null) {
            throw new NotFoundException("Can not find gift by id: " + id);
        }
        if(avatar != null) {
            giftDTO.setAvatar(uploadFile.getUrlFromFile(avatar));
        }

        return giftRepository.save(ConvertObject.convertGiftDTOToGift(gift, giftDTO));
    }

    @Override
    public Gift changeStatusById(Long id) {
        Gift gift = findGiftById(id);
        if(gift == null) {
            throw new NotFoundException("Can not find gift by id: " + id);
        }
        gift.setStatus(!gift.getStatus());
        return giftRepository.save(gift);
    }

    @Override
    public Gift deleteGiftById(Long id) {
        Gift gift = findGiftById(id);
        if(gift == null) {
            throw new NotFoundException("Can not find gift by id: " + id);
        }
        giftRepository.delete(gift);
        return gift;
    }

    @Override
    public Gift changeImageGiftById(Long id, MultipartFile multipartFile) {
        Gift gift = findGiftById(id);
        if(gift == null) {
            throw new NotFoundException("Can not gift by id: " + id);
        }
        if(gift.getAvatar() != null) {
            uploadFile.removeImageFromUrl(gift.getAvatar());
        }
        gift.setAvatar(uploadFile.getUrlFromFile(multipartFile));
        return giftRepository.save(gift);
    }

    @Override
    public List<Gift> getGiftsByKey(String key) {
        try {
            Long mark = Long.parseLong(key);
            return giftRepository.findAllByNameContainingOrMarkContaining(key, mark);
        }catch (Exception e) {
            return giftRepository.findAllByNameContainingOrMarkContaining(key, 0L);
        }
    }

//    @Override
//    public Set<Gift> findAllGiftByUserId(Long userId, boolean active, boolean both) {
//        Optional<User> user = userRepository.findById(userId);
//        if(user.isEmpty()) {
//            throw new NotFoundException("Can not find user by id: " + userId);
//        }
//        Set<Gift> gifts = new HashSet<>();
//        if(both) {
//            gifts = new HashSet<>(giftRepository.findAll());
//        }else if(active) {
//            gifts = giftRepository.findAllByStatus(true);
//        }else {
//            gifts = giftRepository.findAllByStatus(false);
//        }
//
//        if(!gifts.isEmpty()) {
//            gifts = gifts.stream().filter((item) -> {
//                return item.getUsers().contains(user.get());
//            }).collect(Collectors.toSet());
//        }
//
//        return gifts;
//    }
}
