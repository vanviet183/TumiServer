package com.example.tumiweb.controller;

import com.example.tumiweb.storage.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    @Autowired
    private IStorageService storageService;

    @PostMapping("")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        storageService.store(file);
        return file.getOriginalFilename();
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadFileByName(@PathVariable("fileName") String fileName) {
        Resource file = storageService.loadAsResource(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}