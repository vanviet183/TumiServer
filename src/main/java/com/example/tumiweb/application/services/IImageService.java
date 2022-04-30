package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.dto.ImageDTO;
import com.example.tumiweb.domain.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface IImageService {

  Set<Image> findAllImage(Long page, int size);

  Image findImageById(Long id);

  Image createNewImage(ImageDTO imageDTO, MultipartFile multipartFile);

  Image editImageById(Long id, ImageDTO imageDTO, MultipartFile multipartFile);

  Image deleteImageById(Long id);

}
