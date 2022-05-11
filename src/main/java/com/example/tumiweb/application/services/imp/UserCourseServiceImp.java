package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.adapter.web.v1.transfer.response.TrueFalseResponse;
import com.example.tumiweb.application.constants.DevMessageConstant;
import com.example.tumiweb.application.constants.UserMessageConstant;
import com.example.tumiweb.application.dai.CourseRepository;
import com.example.tumiweb.application.dai.UserRepository;
import com.example.tumiweb.application.services.IUserCourseService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.entity.Course;
import com.example.tumiweb.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserCourseServiceImp implements IUserCourseService {
  private final CourseRepository courseRepository;
  private final UserRepository userRepository;

  public UserCourseServiceImp(CourseRepository courseRepository, UserRepository userRepository) {
    this.courseRepository = courseRepository;
    this.userRepository = userRepository;
  }

  @Override
  public TrueFalseResponse registerCourseByUserIdAndCourseId(Long userId, Long courseId) {
    Course course = findCourseById(courseId);
    User user = findUserById(userId);

    Set<Course> newCourse = user.getCourses();
    newCourse.add(course);

    Set<User> newUsers = course.getUsers();
    newUsers.add(user);

    course.setUsers(newUsers);
    user.setCourses(newCourse);

    userRepository.save(user);
    courseRepository.save(course);

    return new TrueFalseResponse(true);
  }

  @Override
  public TrueFalseResponse cancelCourseByUserIdAndCourseId(Long userId, Long courseId) {
    Course course = findCourseById(courseId);
    User user = findUserById(userId);

    Set<Course> newCourse = user.getCourses();
    newCourse.remove(course);

    Set<User> newUsers = course.getUsers();
    newUsers.remove(user);

    course.setUsers(newUsers);
    user.setCourses(newCourse);

    userRepository.save(user);
    courseRepository.save(course);

    return new TrueFalseResponse(true);
  }

  @Override
  public Set<Course> findAllCourseByUserId(Long userId, boolean status, boolean both) {
    return findUserById(userId).getCourses();
  }

  private User findUserById(Long id) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isEmpty()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "user", id));
    }
    return userOptional.get();
  }

  private Course findCourseById(Long id) {
    Optional<Course> courseOptional = courseRepository.findById(id);
    if (courseOptional.isEmpty()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "course", id));
    }
    return courseOptional.get();
  }

}
