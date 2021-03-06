package com.example.tumiweb.application.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {

  void init();

  void store(MultipartFile file);

  Resource loadAsResource(String filename);

}
