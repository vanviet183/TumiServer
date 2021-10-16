package com.example.tumiweb.services.imp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.tumiweb.dto.UserDTO;
import com.example.tumiweb.exception.DuplicateException;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.model.Course;
import com.example.tumiweb.model.User;
import com.example.tumiweb.repository.CourseRepository;
import com.example.tumiweb.repository.UserRepository;
import com.example.tumiweb.services.IUserService;
import com.example.tumiweb.utils.ConvertObject;
import com.example.tumiweb.utils.UploadImage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class UserServiceImp implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CourseRepository courseRepository;

    private User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    @Override
    public Set<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.size() == 0) {
            throw new NotFoundException("User list is empty");
        }
        return new HashSet<>(users);
    }

    @Override
    public Set<User> getAllUsersWithPage(Long page, int size) {
        Page<User> usersOfPage = userRepository.findAll(PageRequest.of(page.intValue(), size));
        List<User> users = usersOfPage.getContent();
        if(users.isEmpty()) {
            throw new NotFoundException("User list is empty");
        }
        return new HashSet<>(users);
    }

    @Override
    public User getUserById(Long id) {
        User user = findUserById(id);
        if(user == null) {
            throw new NotFoundException("Can not find user by id: " + id);
        }
        return user;
    }

    @Override
    public User createNewUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        if(userRepository.findByUsername(user.getUsername()) != null) {
            throw new DuplicateException("Duplicate username: " + user.getUsername());
        }
        if(userRepository.findByEmail(user.getEmail()) != null) {
            throw new DuplicateException("Duplicate email: " + user.getEmail());
        }

        return userRepository.save(user);
    }

    @Override
    public User forgotPasswordById(Long id, String password) {
        return null;
    }

    @Override
    public User editUserById(Long id, UserDTO userDTO) {
        User user = findUserById(id);
        if(user == null) {
            throw new NotFoundException("Can not find user by id: " + id);
        }

        return userRepository.save(ConvertObject.convertUserDTOToUser(user, userDTO));
    }

    @Override
    public User deleteUserById(Long id) {
        User user = findUserById(id);
        if(user == null) {
            throw new NotFoundException("Can not find user by id: " + id);
        }
        userRepository.delete(user);
        return user;
    }

    @Override
    public User changeStatusById(Long id) {
        User user = findUserById(id);
        if(user == null) {
            throw new NotFoundException("Can not find user by id: " + id);
        }
        user.setStatus(!user.getStatus());
        return userRepository.save(user);
    }

    @Override
    public String changeAvatarById(Long id, MultipartFile avatar) {
        User user = findUserById(id);
        if(user == null) {
            throw new NotFoundException("Can not find user by id: " + id);
        }

        //Xóa avtar cũ
        if(user.getAvatar() != null) {
            UploadImage.removeImageFromUrl(user.getAvatar());
        }

        user.setAvatar(UploadImage.getUrlFromFile(avatar));

        return "Change successfully";
    }

    @Override
    public String registerCourseByUserIdAndCourseId(Long userId, Long courseId) {
        //mặc định user and course có cho nhanh nhé :v
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

        return "Register successfully";
    }

    @Override
    public String cancelCourseByUserIdAndCourseId(Long userId, Long courseId) {
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

        return "Unregister successfully";
    }
}
