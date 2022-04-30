package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.ImageRepository;
import com.example.tumiweb.application.services.IImageService;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.ImageDTO;
import com.example.tumiweb.domain.entity.Image;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ImageServiceImp implements IImageService {
  @Autowired
  private ImageRepository imageRepository;
  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private UploadFile uploadFile;

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
      throw new VsException("Can not find image by id: " + id);
    }
    return image.get();
  }

  @Override
  public Image createNewImage(ImageDTO imageDTO, MultipartFile multipartFile) {
    if (multipartFile.isEmpty()) {
      throw new VsException("File image is empty, can not create Image");
    }
    if (imageRepository.findByTitle(imageDTO.getTitle()) != null) {
      throw new VsException("Duplicate image by title: " + imageDTO.getTitle());
    }
    Image image = modelMapper.map(imageDTO, Image.class);
    image.setPath(uploadFile.getUrlFromFile(multipartFile));
    return imageRepository.save(image);
  }

  @Override
  public Image editImageById(Long id, ImageDTO imageDTO, MultipartFile multipartFile) {
    Image image = findImageById(id);
    if (image == null) {
      throw new VsException("Can not find image by id: " + id);
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
      throw new VsException("Can not find image by id: " + id);
    }
    uploadFile.removeImageFromUrl(image.getPath());
    imageRepository.delete(image);
    return image;
  }

}





























