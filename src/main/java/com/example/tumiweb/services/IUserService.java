package com.example.tumiweb.services;

import com.example.tumiweb.dto.UserDTO;
import com.example.tumiweb.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public interface IUserService {
    Set<User> getAllUsers();
    Set<User> getAllUsersWithPage(Long page, int size);
    User getUserById(Long id);
    User createNewUser(UserDTO userDTO);
    User forgotPasswordById(Long id, String password);
    User editUserById(Long id, UserDTO userDTO);
    User deleteUserById(Long id);
    User changeStatusById(Long id);
    String changeAvatarById(Long id, MultipartFile avatar) throws IOException;
}
