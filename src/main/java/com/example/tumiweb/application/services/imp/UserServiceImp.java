package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.adapter.web.v1.transfer.response.TrueFalseResponse;
import com.example.tumiweb.application.constants.EmailConstant;
import com.example.tumiweb.application.constants.RoleConstant;
import com.example.tumiweb.application.dai.UserRepository;
import com.example.tumiweb.application.mapper.UserMapper;
import com.example.tumiweb.application.services.ISendMailService;
import com.example.tumiweb.application.services.IUserService;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.UserDTO;
import com.example.tumiweb.domain.entity.Role;
import com.example.tumiweb.domain.entity.User;
import com.example.tumiweb.domain.entity.base.AbstractAuditingEntity;
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
  public List<User> getAllUsers(Long page, int size, boolean activeFlag, boolean both) {
    List<User> users;
    if (page != null) {
      users = userRepository.findAll(PageRequest.of(page.intValue(), size)).getContent();
    } else {
      users = userRepository.findAll();
    }

    if (both) {
      return users;
    }

    if (activeFlag) {
      return users.parallelStream().filter(AbstractAuditingEntity::getActiveFlag).collect(Collectors.toList());
    }
    return users;
  }

  //  @Cacheable(value = "user", key = "#id")
  @Override
  public User getUserById(Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isEmpty()) {
      throw new VsException("exception.general", "invalid.general.required");
    }
    if (user.get().getDeleteFlag()) {
      throw new VsException("This user was delete");
    }
    return user.get();
  }

  //  @Cacheable(value = "user", key = "#username")
  @Override
  public User getUserByUsername(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new VsException("Can not find user by username: " + username);
    }
    return user;
  }

  //  @Cacheable(value = "user", key = "#email")
  @Override
  public User getByEmail(String email) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new VsException("Can not find user by email: " + email);
    }
    return user;
  }

  //  @CacheEvict(value = "user", allEntries = true)
  @Override
  public User createNewUser(UserDTO userDTO) {
    User user = userMapper.toUser(userDTO, null);
    if (userRepository.findByUsername(user.getUsername()) != null) {
      throw new VsException("Can not create user because duplicate username: " + user.getUsername());
    }
    if (userRepository.findByEmail(user.getEmail()) != null) {
      throw new VsException("Can not create user because duplicate email: " + user.getEmail());
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
      throw new VsException("Can not find user by this token");
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
        if (role.getName().equals(RoleConstant.ADMIN_NAME)) {
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
    return userRepository.findAllByBirthday(birthday);
  }

}
