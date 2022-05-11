package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.adapter.web.v1.transfer.response.TrueFalseResponse;
import com.example.tumiweb.application.constants.DevMessageConstant;
import com.example.tumiweb.application.constants.EmailConstant;
import com.example.tumiweb.application.constants.RoleConstant;
import com.example.tumiweb.application.constants.UserMessageConstant;
import com.example.tumiweb.application.dai.UserRepository;
import com.example.tumiweb.application.input.common.GetAllDataInput;
import com.example.tumiweb.application.mapper.UserMapper;
import com.example.tumiweb.application.output.common.GetAllDataOutput;
import com.example.tumiweb.application.services.ISendMailService;
import com.example.tumiweb.application.services.IUserService;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.UserDTO;
import com.example.tumiweb.domain.entity.Role;
import com.example.tumiweb.domain.entity.User;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements IUserService {
  private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
  private final UserRepository userRepository;
  private final UploadFile uploadFile;
  private final ISendMailService sendMailService;

  public UserServiceImp(UserRepository userRepository, UploadFile uploadFile, ISendMailService sendMailService) {
    this.userRepository = userRepository;
    this.uploadFile = uploadFile;
    this.sendMailService = sendMailService;
  }

  //  @Cacheable(value = "user", key = "'all'")
  @Override
  public GetAllDataOutput<User> getAllUsers(GetAllDataInput input) {
    List<User> users;
    if (input.getPage() != null) {
      users = userRepository.findAll(PageRequest.of(input.getPage().intValue(), input.getSizeOfPage())).getContent();
    } else {
      users = userRepository.findAll();
    }

    if (input.getBoth() != null && input.getBoth()) {
      return new GetAllDataOutput<>(users);
    }

    if (input.getActiveFlag() != null) {
      users =
          users.parallelStream().filter(item -> item.getActiveFlag().equals(input.getActiveFlag())).collect(Collectors.toList());
    }
    return new GetAllDataOutput<>(users);
  }

  //  @Cacheable(value = "user", key = "#id")
  @Override
  public User getUserById(Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isEmpty()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "user", id));
    }
    if (user.get().getDeleteFlag()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.DATA_WAS_DELETE, id));
    }
    return user.get();
  }

  //  @Cacheable(value = "user", key = "#username")
  @Override
  public User getUserByUsername(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.User.NOT_FOUND_USER_BY_USERNAME, username));
    }
    return user;
  }

  //  @Cacheable(value = "user", key = "#email")
  @Override
  public User getByEmail(String email) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.User.NOT_FOUND_USER_BY_EMAIL, email));
    }
    return user;
  }

  //  @CacheEvict(value = "user", allEntries = true)
  @Override
  public User createNewUser(UserDTO userDTO) {
    User user = userMapper.toUser(userDTO, null);
    if (userRepository.findByUsername(user.getUsername()) != null) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.User.CAN_NOT_CREATE_USER_BY_USERNAME, user.getUsername()));
    }
    if (userRepository.findByEmail(user.getEmail()) != null) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.User.CAN_NOT_CREATE_USER_BY_EMAIL, user.getEmail()));
    }

    String contentMail = "Đây là tài khoản của bạn, không được chia sẻ cho bất cứ ai.\nUsername: "
        + user.getUsername() + "\nPassword: " + user.getPassword()
        + "\n\nCảm ơn bạn đã đăng ký học tại Tumi !";
    sendMailService.sendMailWithText(EmailConstant.SUBJECT_REGISTER, contentMail, user.getEmail());

    return userRepository.save(user);
  }

  //  @CacheEvict(value = "user", allEntries = true)
  @Override
  public User editUserById(Long id, UserDTO userDTO) {
    User user = getUserById(id);
    return userRepository.save(userMapper.toUser(userDTO, user.getId()));
  }

  //  @CacheEvict(value = "user", allEntries = true)
  @Override
  public TrueFalseResponse deleteUserById(Long id) {
    User user = getUserById(id);
    userRepository.delete(user);
    return new TrueFalseResponse(true);
  }

  //  @CacheEvict(value = "user", allEntries = true)
  @Override
  public User changeDeleteFlagById(Long id) {
    User user = getUserById(id);
    user.setDeleteFlag(!user.getDeleteFlag());
    return userRepository.save(user);
  }

  //  @CacheEvict(value = "user", allEntries = true)
  @Override
  public User changeAvatarById(Long id, MultipartFile avatar) {
    User user = getUserById(id);

    if (user.getAvatar() != null) {
      uploadFile.removeImageFromUrl(user.getAvatar());
    }
    user.setAvatar(uploadFile.getUrlFromFile(avatar));

    return userRepository.save(user);
  }

  //  @CacheEvict(value = "user", allEntries = true)
  @Override
  public TrueFalseResponse changeMarkById(Long id, Long mark) {
    User user = getUserById(id);
    user.setMark(user.getMark() + mark);
    if (user.getMark() < 0) {
      return new TrueFalseResponse(false);
    }
    userRepository.save(user);
    return new TrueFalseResponse(true);
  }

  @Override
  public User getUserByTokenResetPass(String token) {
    User user = userRepository.findByTokenResetPass(token);
    if (user == null) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.User.NOT_FOUND_USER_BY_TOKEN_PASS, token));
    }
    return user;
  }

  //  @CacheEvict(value = "user", key = "#user.id")
  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  //  @Cacheable(value = "user", key = "'admin'")
  @Override
  public List<User> getAllUserAdmin() {
    return userRepository.findAll().parallelStream().filter(user -> {
      Set<Role> roles = user.getRoles();
      for (Role role : roles) {
        if (role.getName().equals(RoleConstant.ADMIN_NAME) && !user.getDeleteFlag()) {
          return true;
        }
      }
      return false;
    }).collect(Collectors.toList());
  }

  //  @Cacheable(value = "user", key = "#key")
  @Override
  public List<User> searchUsersByKey(String key) {
    return userRepository.findAllByUsernameContainingOrEmailContainingOrPhoneContaining(key, key, key);
  }

  //  @Cacheable(value = "user", key = "#birthday")
  @Override
  public List<User> getAllUserByBirthday(String birthday) {
    return userRepository.findAllByBirthdayAndDeleteFlagAndActiveFlag(birthday, false, true);
  }

}
