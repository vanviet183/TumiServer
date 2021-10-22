package com.example.tumiweb.services;

import com.example.tumiweb.dto.UserDTO;
import com.example.tumiweb.dao.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public interface IUserService {
    Set<User> getAllUsers(Long page, int size, boolean status, boolean both);
    User getUserById(Long id);
    User getUserByUsername(String username);
    User createNewUser(UserDTO userDTO);
    //change password chờ security làm 1 thể
    User forgotPasswordById(Long id, String password);
    User editUserById(Long id, UserDTO userDTO);
    User deleteUserById(Long id);
    User changeStatusById(Long id);
    String changeAvatarById(Long id, MultipartFile avatar) throws IOException;
    Boolean changeMarkById(Long id, Long mark);

    User save(User user);

    //
    String registerCourseByUserIdAndCourseId(Long userId, Long courseId);
    String cancelCourseByUserIdAndCourseId(Long userId, Long courseId);
}
