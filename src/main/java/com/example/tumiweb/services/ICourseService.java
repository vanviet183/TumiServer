package com.example.tumiweb.services;

import com.example.tumiweb.dto.CourseDTO;
import com.example.tumiweb.dao.Course;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface ICourseService {
    Set<Course> findAllCourse(Long page, int size, boolean status, boolean both);
    Set<Course> findAllCourseByUserId(Long userId, boolean status, boolean both);
    Course createNewCourse(CourseDTO courseDTO ,MultipartFile multipartFile, Long categoryId); //nhớ image nhé(note thôi)
    Course findCourseById(Long id);
    Course changeAvatarCourseById(Long id, MultipartFile multipartFile);
    Course editCourseById(Long id, CourseDTO courseDTO, MultipartFile multipartFile);
    Course deleteCourseById(Long id);
    Course changeStatusById(Long id);

    Course editCategoryById(Long courseId, Long categoryId);
}