package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.GiftDTO;
import com.example.tumiweb.exception.DuplicateException;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.model.Gift;
import com.example.tumiweb.model.User;
import com.example.tumiweb.repository.GiftRepository;
import com.example.tumiweb.repository.UserRepository;
import com.example.tumiweb.services.IGiftService;
import com.example.tumiweb.utils.UploadImage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
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
    private UserRepository userRepository;

    @Override
    public Set<Gift> getAllGift(Long page, int size, boolean active) {
        List<Gift> gifts;
        if(page != null) {
            Page<Gift> giftPage = giftRepository.findAll(PageRequest.of(page.intValue(), size));
            gifts = giftPage.getContent();
        }else {
            gifts = giftRepository.findAll();
        }

        if(gifts.isEmpty()) {
            throw new NotFoundException("Gift list is empty");
        }

        if(active) {
            gifts = gifts.stream().filter(item -> item.getStatus()).collect(Collectors.toList());
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
    public Gift createNewGift(GiftDTO giftDTO, MultipartFile avatar) {
        if(giftRepository.findByName(giftDTO.getName()) != null) {
            throw new DuplicateException("Duplicate gift with title: " + giftDTO.getName());
        }
        if(avatar != null) {
            giftDTO.setAvatar(UploadImage.getUrlFromFile(avatar));
        }
        return giftRepository.save(modelMapper.map(giftDTO, Gift.class));
    }

    @Override
    public Gift editGiftById(Long id, GiftDTO giftDTO, MultipartFile avatar) {
        Gift gift = findGiftById(id);
        if(gift == null) {
            throw new NotFoundException("Can not find gift by id: " + id);
        }
        if(avatar != null) {
            giftDTO.setAvatar(UploadImage.getUrlFromFile(avatar));
        }
        return giftRepository.save(modelMapper.map(giftDTO, Gift.class));
    }

    @Override
    public Gift changeStatusById(Long id) {
        Gift gift = findGiftById(id);
        if(gift == null) {
            throw new NotFoundException("Can not find gift by id: " + id);
        }
        gift.setStatus(!gift.getStatus());
        return gift;
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
    public Set<Gift> findAllGiftByUserId(Long userId, boolean active, boolean both) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new NotFoundException("Can not find user by id: " + userId);
        }
        Set<Gift> gifts = new HashSet<>();
        if(both) {
            gifts = new HashSet<>(giftRepository.findAll());
        }else if(active) {
            gifts = giftRepository.findAllByStatus(true);
        }else {
            gifts = giftRepository.findAllByStatus(false);
        }

        if(!gifts.isEmpty()) {
            gifts = gifts.stream().filter((item) -> {
                return item.getUsers().contains(user.get());
            }).collect(Collectors.toSet());
        }

        return gifts;
    }
}
