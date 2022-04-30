package com.example.tumiweb.application.storage;

import com.example.tumiweb.application.dai.DBFileRepository;
import com.example.tumiweb.config.exception.VsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileSystemStorageService implements IStorageService {
  private final Path rootLocation;

  @Autowired
  private DBFileRepository fileStorageRepository;

  @Autowired
  public FileSystemStorageService(StorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
  }

  @Override
  public void init() {
    try {
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new VsException("Could not initialize storage");
    }
  }

  @Override
  public void store(MultipartFile file) {
    try {
      //Save to project
      if (file.isEmpty()) {
        throw new VsException("Failed to store empty file.");
      }
      Path destinationFile = this.rootLocation.resolve(
          Paths.get(Objects.requireNonNull(file.getOriginalFilename())))
          .normalize().toAbsolutePath();
      if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
        // This is a security check
        throw new VsException("Cannot store file outside current directory.");
      }
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, destinationFile,
            StandardCopyOption.REPLACE_EXISTING);
      }

      //Save to A

    } catch (IOException e) {
      throw new VsException("Failed to store file.");
    }
  }

  public Path load(String filename) {
    return rootLocation.resolve(filename);
  }

  @Override
  public Resource loadAsResource(String filename) {
    try {
      Path file = load(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new VsException("Could not read file: " + filename);
      }
    } catch (MalformedURLException e) {
      throw new VsException("Could not read file: " + filename);
    }
  }

}
