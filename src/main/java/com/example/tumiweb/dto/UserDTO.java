package com.example.tumiweb.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;
}
