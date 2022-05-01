package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.GiftRepository;
import com.example.tumiweb.application.mapper.GiftMapper;
import com.example.tumiweb.application.services.IGiftService;
import com.example.tumiweb.application.utils.ConvertObject;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.GiftDTO;
import com.example.tumiweb.domain.entity.Gift;
import com.example.tumiweb.domain.entity.base.AbstractAuditingEntity;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftServiceImp implements IGiftService {
  private final GiftMapper giftMapper = Mappers.getMapper(GiftMapper.class);
  @Autowired
  private GiftRepository giftRepository;
  @Autowired
  private UploadFile uploadFile;

  //  @Cacheable(value = "gift", key = "'all'")
  @Override
  public Set<Gift> getAllGift(Long page, int size, boolean active) {
    List<Gift> gifts;
    if (page != null) {
      Page<Gift> giftPage = giftRepository.findAll(PageRequest.of(page.intValue(), size));
      gifts = giftPage.getContent();
    } else {
      gifts = giftRepository.findAll();
    }

    if (active) {
      gifts = gifts.stream().filter(AbstractAuditingEntity::getActiveFlag).collect(Collectors.toList());
    }

    return new HashSet<>(gifts);
  }

  @Override
  public Gift findGiftById(Long id) {
    Optional<Gift> gift = giftRepository.findById(id);
    if (gift.isEmpty()) {
      return null;
    }
    return gift.get();
  }

  //  @CacheEvict(value = "gift", allEntries = true)
  @Override
  public Gift createNewGift(GiftDTO giftDTO, MultipartFile file) {
    if (giftRepository.findByName(giftDTO.getName()) != null) {
      throw new VsException("Duplicate gift with title: " + giftDTO.getName());
    }
    Gift gift = giftMapper.toGift(giftDTO);
    if (file != null) {
      gift.setAvatar(uploadFile.getUrlFromFile(file));
    }

    return giftRepository.save(gift);
  }

  //  @CacheEvict(value = "gift", allEntries = true)
  @Override
  public Gift editGiftById(Long id, GiftDTO giftDTO, MultipartFile avatar) {
    Gift gift = findGiftById(id);
    if (gift == null) {
      throw new VsException("Can not find gift by id: " + id);
    }
    if (avatar != null) {
      giftDTO.setAvatar(uploadFile.getUrlFromFile(avatar));
    }

    return giftRepository.save(ConvertObject.convertGiftDTOToGift(gift, giftDTO));
  }

  //  @CacheEvict(value = "gift", allEntries = true)
  @Override
  public Gift changeStatusById(Long id) {
    Gift gift = findGiftById(id);
    if (gift == null) {
      throw new VsException("Can not find gift by id: " + id);
    }
    gift.setDeleteFlag(!gift.getDeleteFlag());
    return giftRepository.save(gift);
  }

  //  @CacheEvict(value = "gift", allEntries = true)
  @Override
  public Gift deleteGiftById(Long id) {
    Gift gift = findGiftById(id);
    if (gift == null) {
      throw new VsException("Can not find gift by id: " + id);
    }
    giftRepository.delete(gift);
    return gift;
  }

  //  @CacheEvict(value = "gift", allEntries = true)
  @Override
  public Gift changeImageGiftById(Long id, MultipartFile multipartFile) {
    Gift gift = findGiftById(id);
    if (gift == null) {
      throw new VsException("Can not gift by id: " + id);
    }
    if (gift.getAvatar() != null) {
      uploadFile.removeImageFromUrl(gift.getAvatar());
    }
    gift.setAvatar(uploadFile.getUrlFromFile(multipartFile));
    return giftRepository.save(gift);
  }

  //  @Cacheable(value = "gift", key = "#key")
  @Override
  public List<Gift> getGiftsByKey(String key) {
    try {
      Long mark = Long.parseLong(key);
      return giftRepository.findAllByNameContainingOrMarkContaining(key, mark);
    } catch (Exception e) {
      return giftRepository.findAllByNameContainingOrMarkContaining(key, 0L);
    }
  }

  @Override
  public Gift save(Gift gift) {
    return giftRepository.save(gift);
  }

}
