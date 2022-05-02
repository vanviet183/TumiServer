package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.DBFileRepository;
import com.example.tumiweb.application.services.IDBFileStorageService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.entity.DBFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DBFileStorageServiceImp implements IDBFileStorageService {
  private final DBFileRepository dbFileRepository;

  public DBFileStorageServiceImp(DBFileRepository dbFileRepository) {
    this.dbFileRepository = dbFileRepository;
  }

  //  @CacheEvict(value = "dbfile", allEntries = true)
  @Override
  public DBFile storeFile(MultipartFile file, Long uploadBy) {
    // Normalize file name
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    try {
      // Check if the file's name contains invalid characters
      if (fileName.contains("..")) {
        throw new VsException("Sorry! Filename contains invalid path sequence " + fileName);
      }

      DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes(), uploadBy);

      return dbFileRepository.save(dbFile);
    } catch (IOException ex) {
      throw new VsException("Could not store file " + fileName + ". Please try again!");
    }
  }

  //  @Cacheable(value = "dbfile", key = "#fileId")
  @Override
  public DBFile getFile(String fileId) {
    return dbFileRepository.findById(fileId)
        .orElseThrow(() -> new VsException("File not found with id " + fileId));
  }

  @Override
  public DBFile save(DBFile dbFile) {
    return dbFileRepository.save(dbFile);
  }
}
