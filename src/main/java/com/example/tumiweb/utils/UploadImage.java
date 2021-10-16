package com.example.tumiweb.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.tumiweb.exception.UploadImageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public class UploadImage {
    @Autowired
    private static Cloudinary cloudinary;

    public static String getUrlFromFile(MultipartFile multipartFile) {
        try {
            Map<?, ?> map = cloudinary.uploader().upload(multipartFile, ObjectUtils.emptyMap());
            return map.get("secure_url").toString();
        } catch (IOException e) {
            throw new UploadImageException("Upload image failed");
        }
    }

    public static void removeImageFromUrl(String url) {
        try {
            cloudinary.uploader().destroy(url, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new UploadImageException("Upload image failed");
        }
    }
}
