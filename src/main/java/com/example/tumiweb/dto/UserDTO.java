package com.example.tumiweb.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private Long mark;
    private String birthday;
    private String password;
}
