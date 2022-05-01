package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.constants.EmailConstant;
import com.example.tumiweb.application.dai.CourseRepository;
import com.example.tumiweb.application.dai.UserRepository;
import com.example.tumiweb.application.mapper.UserMapper;
import com.example.tumiweb.application.services.ISendMailService;
import com.example.tumiweb.application.services.IUserService;
import com.example.tumiweb.application.utils.ConvertObject;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.UserDTO;
import com.example.tumiweb.domain.entity.Course;
import com.example.tumiweb.domain.entity.Role;
import com.example.tumiweb.domain.entity.User;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements IUserService {
  private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CourseRepository courseRepository;
  @Autowired
  private UploadFile uploadFile;
  @Autowired
  private ISendMailService sendMailService;

  private User findUserById(Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isEmpty()) {
      return null;
    }
    return user.get();
  }

  //  @Cacheable(value = "user", key = "'all'")
  @Override
  public Set<User> getAllUsers(Long page, int size, boolean status, boolean both) {
    Set<User> users = new HashSet<>();
    //lấy cả 2 true and false
    if (both) {
      if (page == null) {
        users = new HashSet<>(userRepository.findAll());
      } else {
        users = new HashSet<>(userRepository.findAll(PageRequest.of(page.intValue(), size)).getContent());
      }
    } else if (status) { //lấy 1 kiểu active
      if (page == null) {
        //true and no paging
        users = userRepository.findAllByDeleteFlag(true);
      } else {
        //true and paging
        users = userRepository.findAllByDeleteFlag(true);
        int length = users.size();
        int totalPage = (length % page != 0) ? length / size + 1 : length / size;
        if (totalPage > page.intValue()) {
          return new HashSet<>();
        }
        users = new HashSet<>(new ArrayList<>(users).subList(page.intValue() * size, page.intValue() * size + size));
      }
    } else {
      users = new HashSet<>(userRepository.findAll());
    }
    return users;
  }

  //  @Cacheable(value = "user", key = "#id")
  @Override
  public User getUserById(Long id) {
    System.out.println("get by id user");
    User user = findUserById(id);
    if (user == null) {
      throw new VsException("Can not find user by id: " + id);
    }
    return user;
  }

  //  @Cacheable(value = "user", key = "#username")
  @Override
  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  //  @Cacheable(value = "user", key = "#email")
  @Override
  public User getByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  //  @CacheEvict(value = "user", allEntries = true)
  @Override
  public User createNewUser(UserDTO userDTO) {
    User user = userMapper.toUser(userDTO);
    if (userRepository.findByUsername(user.getUsername()) != null) {
      throw new VsException("Duplicate username: " + user.getUsername());
    }
    if (userRepository.findByEmail(user.getEmail()) != null) {
      throw new VsException("Duplicate email: " + user.getEmail());
    }

    //send mail tạo tài khoản thành công
    String contentMail = "Đây là tài khoản của bạn, không được chia sẻ cho bất cứ ai.\nUsername: "
        + user.getUsername() + "\nPassword: " + user.getPassword()
        + "\n\nCảm ơn bạn đã đăng ký học tại Tumi !";
    sendMailService.sendMailWithText(EmailConstant.SUBJECT_REGISTER, contentMail, user.getEmail());
    //

    return userRepository.save(user);
  }

  //  @CacheEvict(value = "user", allEntries = true)
  @Override
  public User editUserById(Long id, UserDTO userDTO) {
    User user = findUserById(id);
    if (user == null) {
      throw new VsException("Can not find user by id: " + id);
    }

    return userRepository.save(ConvertObject.convertUserDTOToUser(user, userDTO));
  }

  //  @CacheEvict(value = "user", allEntries = true)
  @Override
  public User deleteUserById(Long id) {
    User user = findUserById(id);
    if (user == null) {
      throw new VsException("Can not find user by id: " + id);
    }
    userRepository.delete(user);
    return user;
  }

  //  @CacheEvict(value = "user", allEntries = true)
  @Override
  public User changeStatusById(Long id) {
    User user = findUserById(id);
    if (user == null) {
      throw new VsException("Can not find user by id: " + id);
    }
//    user.setStatus(!user.getStatus());
    return userRepository.save(user);
  }

  //  @CacheEvict(value = "user", allEntries = true)
  @Override
  public String changeAvatarById(Long id, MultipartFile avatar) throws IOException {
    User user = findUserById(id);
    if (user == null) {
      throw new VsException("Can not find user by id: " + id);
    }

    //Xóa avtar cũ
    if (user.getAvatar() != null) {
      uploadFile.removeImageFromUrl(user.getAvatar());
    }
    user.setAvatar(uploadFile.getUrlFromFile(avatar));
    userRepository.save(user);

    return "Change successfully";
  }

  //  @CacheEvict(value = "user", allEntries = true)
  @Override
  public Boolean changeMarkById(Long id, Long mark) {
    User user = getUserById(id);
    if (user == null) {
      throw new VsException("Can not find user by id: " + id);
    }
    long newMark = user.getMark() + mark;
    if (newMark < 0) {
      return false;
    }
    user.setMark(newMark);
    userRepository.save(user);
    return true;
  }

  @Override
  public User getUserByTokenResetPass(String token) {
    return userRepository.findByTokenResetPass(token);
  }

  //  @CacheEvict(value = "user", key = "#user.id")
  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  //  @Cacheable(value = "user", key = "'admin'")
  @Override
  public List<User> getAllUserAdmin() {
    return userRepository.findAll().stream().filter(user -> {
      List<Role> roles = new ArrayList<>(user.getRoles());
      for (Role role : roles) {
        if (role.getName().compareTo("ROLE_ADMIN") == 0) {
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

  //  @CacheEvict(value = "user", key = "'all'")
  @Override
  public String registerCourseByUserIdAndCourseId(Long userId, Long courseId) {
    //mặc định user and course có cho nhanh nhé :v
    try {
      Course course = courseRepository.getById(courseId);
      User user = userRepository.getById(userId);

      Set<Course> newCourse = user.getCourses();
      newCourse.add(course);

      Set<User> newUsers = course.getUsers();
      newUsers.add(user);

      course.setUsers(newUsers);
      user.setCourses(newCourse);

      userRepository.save(user);
      courseRepository.save(course);
    } catch (Exception e) {
      return "Register failed";
    }

    return "Register successfully";
  }

  //  @CacheEvict(value = "user", key = "'all'")
  @Override
  public String cancelCourseByUserIdAndCourseId(Long userId, Long courseId) {
    try {
      //mặc định user and course có cho nhanh nhé :v
      Course course = courseRepository.getById(courseId);
      User user = userRepository.getById(userId);

      Set<Course> newCourse = user.getCourses();
      newCourse.remove(course);

      Set<User> newUsers = course.getUsers();
      newUsers.remove(user);

      course.setUsers(newUsers);
      user.setCourses(newCourse);

      userRepository.save(user);
      courseRepository.save(course);
    } catch (Exception e) {
      return "Unregister failed";
    }

    return "Unregister successfully";
  }

}
