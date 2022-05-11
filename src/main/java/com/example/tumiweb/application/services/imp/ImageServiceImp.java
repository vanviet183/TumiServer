package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.constants.DevMessageConstant;
import com.example.tumiweb.application.constants.UserMessageConstant;
import com.example.tumiweb.application.dai.ImageRepository;
import com.example.tumiweb.application.mapper.ImageMapper;
import com.example.tumiweb.application.services.IImageService;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.ImageDTO;
import com.example.tumiweb.domain.entity.Image;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ImageServiceImp implements IImageService {
  private final ImageMapper imageMapper = Mappers.getMapper(ImageMapper.class);
  private final ImageRepository imageRepository;
  private final UploadFile uploadFile;

  public ImageServiceImp(ImageRepository imageRepository, UploadFile uploadFile) {
    this.imageRepository = imageRepository;
    this.uploadFile = uploadFile;
  }

  @Override
  public Set<Image> findAllImage(Long page, int size) {
    if (page != null) {
      return new HashSet<>(imageRepository.findAll(PageRequest.of(page.intValue(), size)).getContent());
    }
    return new HashSet<>(imageRepository.findAll());
  }

  @Override
  public Image findImageById(Long id) {
    Optional<Image> image = imageRepository.findById(id);
    if (image.isEmpty()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "image", id));
    }
    return image.get();
  }

  @Override
  public Image createNewImage(ImageDTO imageDTO, MultipartFile multipartFile) {
    if (multipartFile.isEmpty()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL, DevMessageConstant.Common.FILE_IS_EMPTY);
    }
    if (imageRepository.findByTitle(imageDTO.getTitle()) != null) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.DUPLICATE_NAME, imageDTO.getTitle()));
    }
    Image image = imageMapper.toImage(imageDTO);
    image.setPath(uploadFile.getUrlFromFile(multipartFile));
    return imageRepository.save(image);
  }

  @Override
  public Image editImageById(Long id, ImageDTO imageDTO, MultipartFile multipartFile) {
    Image image = findImageById(id);
    if (image == null) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "image", id));
    }
    image.setTitle(imageDTO.getTitle());
    if (!multipartFile.isEmpty()) {
      image.setPath(uploadFile.getUrlFromFile(multipartFile));
    }
    return imageRepository.save(image);
  }

  @Override
  public Image deleteImageById(Long id) {
    Image image = findImageById(id);
    if (image == null) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "image", id));
    }
    uploadFile.removeImageFromUrl(image.getPath());
    imageRepository.delete(image);
    return image;
  }

}





























