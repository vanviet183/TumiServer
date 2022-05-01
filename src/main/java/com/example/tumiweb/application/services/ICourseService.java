package com.example.tumiweb.application.services;

import com.example.tumiweb.adapter.web.v1.transfer.response.TrueFalseResponse;
import com.example.tumiweb.domain.dto.CourseDTO;
import com.example.tumiweb.domain.entity.Course;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICourseService {

  List<Course> findAllCourse(Long page, int size, boolean status, boolean both);

  Course createNewCourse(CourseDTO courseDTO, MultipartFile multipartFile, Long categoryId);

  Course findCourseById(Long id);

  Course changeAvatarCourseById(Long id, MultipartFile multipartFile);

  Course editCourseById(Long id, CourseDTO courseDTO, MultipartFile multipartFile);

  TrueFalseResponse deleteCourseById(Long id);

  Course changeDeleteFlagById(Long id);

  Course editCategoryById(Long courseId, Long categoryId);

  List<Course> getCoursesByKey(String key);

}
