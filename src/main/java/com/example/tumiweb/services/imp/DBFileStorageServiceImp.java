package com.example.tumiweb.services.imp;

import com.example.tumiweb.dao.DBFile;
import com.example.tumiweb.exception.StorageException;
import com.example.tumiweb.exception.StorageFileNotFoundException;
import com.example.tumiweb.repository.DBFileRepository;
import com.example.tumiweb.services.IDBFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DBFileStorageServiceImp implements IDBFileStorageService {
    @Autowired
    private DBFileRepository dbFileRepository;

    @CacheEvict(value = "dbfile", allEntries = true)
    @Override
    public DBFile storeFile(MultipartFile file, Long uploadBy) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new StorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes(), uploadBy);

            return dbFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new StorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Cacheable(value = "dbfile", key = "#fileId")
    @Override
    public DBFile getFile(String fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new StorageFileNotFoundException("File not found with id " + fileId));
    }

    @Override
    public DBFile save(DBFile dbFile) {
        return dbFileRepository.save(dbFile);
    }
}
