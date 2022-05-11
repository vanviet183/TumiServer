package com.example.tumiweb.application.services;

import com.example.tumiweb.adapter.web.v1.transfer.response.TrueFalseResponse;
import com.example.tumiweb.application.input.common.GetAllDataInput;
import com.example.tumiweb.application.output.common.GetAllDataOutput;
import com.example.tumiweb.domain.dto.UserDTO;
import com.example.tumiweb.domain.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {

  GetAllDataOutput getAllUsers(GetAllDataInput input);

  User getUserById(Long id);

  User getUserByUsername(String username);

  User getByEmail(String email);

  User createNewUser(UserDTO userDTO);

  User editUserById(Long id, UserDTO userDTO);

  TrueFalseResponse deleteUserById(Long id);

  User changeDeleteFlagById(Long id);

  User changeAvatarById(Long id, MultipartFile avatar) throws IOException;

  TrueFalseResponse changeMarkById(Long id, Long mark);

  User getUserByTokenResetPass(String token);

  User save(User user);

  List<User> getAllUserAdmin();

  List<User> searchUsersByKey(String key);

  List<User> getAllUserByBirthday(String birthday);

}
