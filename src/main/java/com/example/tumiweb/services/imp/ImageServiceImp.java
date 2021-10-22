package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.ImageDTO;
import com.example.tumiweb.exception.DuplicateException;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.exception.UploadImageException;
import com.example.tumiweb.dao.Image;
import com.example.tumiweb.repository.ImageRepository;
import com.example.tumiweb.services.IImageService;
import com.example.tumiweb.utils.UploadFile;
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
        if(page != null) {
            return new HashSet<>(imageRepository.findAll(PageRequest.of(page.intValue(), size)).getContent());
        }
        return new HashSet<>(imageRepository.findAll());
    }

    @Override
    public Image findImageById(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if(image.isEmpty()) {
            throw new NotFoundException("Can not find image by id: " + id);
        }
        return image.get();
    }

    @Override
    public Image createNewImage(ImageDTO imageDTO, MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) {
            throw new UploadImageException("File image is empty, can not create Image");
        }
        if(imageRepository.findByTitle(imageDTO.getTitle()) != null) {
            throw new DuplicateException("Duplicate image by title: " + imageDTO.getTitle());
        }
        Image image = modelMapper.map(imageDTO, Image.class);
        image.setPath(uploadFile.getUrlFromFile(multipartFile));
        return imageRepository.save(image);
    }

    @Override
    public Image editImageById(Long id, ImageDTO imageDTO, MultipartFile multipartFile) {
        Image image = findImageById(id);
        if(image == null) {
            throw new NotFoundException("Can not find image by id: " + id);
        }
        image.setTitle(imageDTO.getTitle());
        if(!multipartFile.isEmpty()) {
            image.setPath(uploadFile.getUrlFromFile(multipartFile));
        }
        return imageRepository.save(image);
    }

    @Override
    public Image deleteImageById(Long id) {
        Image image = findImageById(id);
        if(image == null) {
            throw new NotFoundException("Can not find image by id: " + id);
        }
        uploadFile.removeImageFromUrl(image.getPath());
        imageRepository.delete(image);
        return image;
    }
}





























