package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.dto.UserDTO;
import com.example.tumiweb.domain.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IUserService {

  Set<User> getAllUsers(Long page, int size, boolean status, boolean both);

  User getUserById(Long id);

  User getUserByUsername(String username);

  User getByEmail(String email);

  User createNewUser(UserDTO userDTO);

  User editUserById(Long id, UserDTO userDTO);

  User deleteUserById(Long id);

  User changeStatusById(Long id);

  String changeAvatarById(Long id, MultipartFile avatar) throws IOException;

  Boolean changeMarkById(Long id, Long mark);

  User getUserByTokenResetPass(String token);

  User save(User user);

  List<User> getAllUserAdmin();

  List<User> searchUsersByKey(String key);

  List<User> getAllUserByBirthday(String birthday);

  //
  String registerCourseByUserIdAndCourseId(Long userId, Long courseId);

  String cancelCourseByUserIdAndCourseId(Long userId, Long courseId);

}
