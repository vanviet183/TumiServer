package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.entity.DBFile;
import org.springframework.web.multipart.MultipartFile;

public interface IDBFileStorageService {

  DBFile storeFile(MultipartFile file, Long uploadBy);

  DBFile getFile(String fileId);

  DBFile save(DBFile dbFile);

}
