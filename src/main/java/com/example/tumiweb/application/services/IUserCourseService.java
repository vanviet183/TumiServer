package com.example.tumiweb.application.services;

import com.example.tumiweb.adapter.web.v1.transfer.response.TrueFalseResponse;
import com.example.tumiweb.domain.entity.Course;

import java.util.Set;

public interface IUserCourseService {

  TrueFalseResponse registerCourseByUserIdAndCourseId(Long userId, Long courseId);

  TrueFalseResponse cancelCourseByUserIdAndCourseId(Long userId, Long courseId);

  Set<Course> findAllCourseByUserId(Long userId, boolean status, boolean both);

}
