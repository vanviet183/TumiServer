package com.example.tumiweb.services.imp;

import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dto.UserDTO;
import com.example.tumiweb.exception.DuplicateException;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.dao.Course;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.repository.CourseRepository;
import com.example.tumiweb.repository.UserRepository;
import com.example.tumiweb.services.ISendMailService;
import com.example.tumiweb.services.IUserService;
import com.example.tumiweb.utils.ConvertObject;
import com.example.tumiweb.utils.UploadFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImp implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UploadFile uploadFile;

    @Autowired
    private ISendMailService sendMailService;

    private User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    @Override
    public Set<User> getAllUsers(Long page, int size, boolean status, boolean both) {
        Set<User> users = new HashSet<>();
        //lấy cả 2 true and false
        if(both) {
            if(page == null) {
                users = new HashSet<>(userRepository.findAll());
            }else {
                users = new HashSet<>(userRepository.findAll(PageRequest.of(page.intValue(), size)).getContent());
            }
        }else if(status) { //lấy 1 kiểu active
            if(page == null) {
                //true and no paging
                users = userRepository.findAllByStatus(true);
            }else {
                //true and paging
                users = userRepository.findAllByStatus(true);
                int length = users.size();
                int totalPage = (length % page != 0) ? length/size + 1 : length/size;
                if(totalPage > page.intValue()) {
                    return new HashSet<>();
                }
                users = new HashSet<>(new ArrayList<>(users).subList(page.intValue()*size, page.intValue()*size + size));
            }
        }else {
            users = new HashSet<>(userRepository.findAll());
        }
        return users;
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
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
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

        //send mail tạo tài khoản thành công
        String contentMail = "Đây là tài khoản của bạn, không được chia sẻ cho bất cứ ai.\nUsername: "
                + user.getUsername() + "\nPassword: " + user.getPassword()
                + "\n\nCảm ơn bạn đã đăng ký học tại Tumi !";
        sendMailService.sendMailWithText(Constants.SUBJECT_REGISTER, contentMail, user.getEmail());
        //

        return userRepository.save(user);
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
    public String changeAvatarById(Long id, MultipartFile avatar) throws IOException {
        User user = findUserById(id);
        if(user == null) {
            throw new NotFoundException("Can not find user by id: " + id);
        }

        //Xóa avtar cũ
        if(user.getAvatar() != null) {
            uploadFile.removeImageFromUrl(user.getAvatar());
        }
        user.setAvatar(uploadFile.getUrlFromFile(avatar));
        userRepository.save(user);

        return "Change successfully";
    }

    @Override
    public Boolean changeMarkById(Long id, Long mark) {
        User user = getUserById(id);
        if(user == null) {
            throw new NotFoundException("Can not find user by id: " + id);
        }
        long newMark = user.getMark() + mark;
        if(newMark < 0) {
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

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public String registerCourseByUserIdAndCourseId(Long userId, Long courseId) {
        //mặc định user and course có cho nhanh nhé :v
        try{
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
        }catch (Exception e) {
            return "Register failed";
        }

        return "Register successfully";
    }

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
        }catch (Exception e) {
            return "Unregister failed";
        }

        return "Unregister successfully";
    }
}
