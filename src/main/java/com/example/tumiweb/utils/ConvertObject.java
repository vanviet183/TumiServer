package com.example.tumiweb.utils;

import com.example.tumiweb.dto.UserDTO;
import com.example.tumiweb.model.User;

public class ConvertObject {
    public static User convertUserDTOToUser(User user, UserDTO userDTO) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        return user;
    }
}
