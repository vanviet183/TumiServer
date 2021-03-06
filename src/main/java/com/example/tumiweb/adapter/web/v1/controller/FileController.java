package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.application.services.IDBFileStorageService;
import com.example.tumiweb.application.storage.IStorageService;
import com.example.tumiweb.domain.entity.DBFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {
  //Save to project
  private final IStorageService storageService;
  //Save to DB
  private final IDBFileStorageService dbFileStorageService;

  public FileController(IStorageService storageService, IDBFileStorageService dbFileStorageService) {
    this.storageService = storageService;
    this.dbFileStorageService = dbFileStorageService;
  }

  // ------------------------- Save to project
  @PostMapping("/proj")
  public String uploadFile(@RequestParam("file") MultipartFile file) {
    storageService.store(file);
    return file.getOriginalFilename();
  }

  @GetMapping("/{fileName}/proj")
  public ResponseEntity<?> downloadFileByName(@PathVariable("fileName") String fileName) {
    Resource file = storageService.loadAsResource(fileName);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }


  // ------------------------- Save to DB
  @PostMapping("/{uploadBy}")
  public ResponseEntity<?> uploadFileToDB(
      @PathVariable(name = "uploadBy", required = false) Long uploadBy,
      @RequestParam("file") MultipartFile file) {
    DBFile dbFile = dbFileStorageService.storeFile(file, uploadBy);

    //ServletUriComponentsBuilder.fromCurrentContextPath(): l???y context path
    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/v1/files/downloadFile/")
        .path(dbFile.getId())
        .toUriString();

    dbFile.setPathDownload(fileDownloadUri);

    return ResponseEntity.status(200).body(dbFileStorageService.save(dbFile));
  }

  @PostMapping("/uploadMultipleFiles/{uploadBy}")
  public List<ResponseEntity<?>> uploadMultipleFiles(@PathVariable(name = "uploadBy", required = false) Long uploadBy,
                                                     @RequestParam("files") MultipartFile[] files) {
    return Arrays.stream(files)
        .map(file -> uploadFileToDB(uploadBy, file))
        .collect(Collectors.toList());
  }

  @GetMapping("/downloadFile/{fileId}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
    // Load file from database
    DBFile dbFile = dbFileStorageService.getFile(fileId);

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(dbFile.getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
        .body(new ByteArrayResource(dbFile.getData()));
  }
}