package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.IUserCourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestApiV1
public class UserCourseController {
  private final IUserCourseService userCourseService;

  public UserCourseController(IUserCourseService userCourseService) {
    this.userCourseService = userCourseService;
  }

  @PostMapping(UrlConstant.UserCourse.DATA_USER_COURSE_REGIS)
  public ResponseEntity<?> registerCourse(@PathVariable("userId") Long userId,
                                          @PathVariable("courseId") Long courseId) {
    return VsResponseUtil.ok(userCourseService.registerCourseByUserIdAndCourseId(userId, courseId));
  }

  @PostMapping(UrlConstant.UserCourse.DATA_USER_COURSE_UN_REGIS)
  public ResponseEntity<?> unRegisterCourse(@PathVariable("userId") Long userId,
                                            @PathVariable("courseId") Long courseId) {
    return VsResponseUtil.ok(userCourseService.cancelCourseByUserIdAndCourseId(userId, courseId));
  }

  @GetMapping(UrlConstant.UserCourse.DATA_USER_COURSE_FIND)
  public ResponseEntity<?> findCourseByUserId(@PathVariable("userId") Long id,
                                              @RequestParam(name = "status", required = false) boolean status,
                                              @RequestParam(name = "both", required = false) boolean both) {
    return VsResponseUtil.ok(userCourseService.findAllCourseByUserId(id, status, both));
  }

}
