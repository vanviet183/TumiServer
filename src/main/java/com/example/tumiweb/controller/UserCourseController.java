package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user-course")
public class UserCourseController extends BaseController<String> {

    @Autowired
    private IUserService userService;

    @GetMapping("/register/{userId}/{courseId}")
    public ResponseEntity<?> registerCourse(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId
    ) {
        return this.resSuccess(userService.registerCourseByUserIdAndCourseId(userId, courseId));
    }
    @GetMapping("/unregister/{userId}/{courseId}")
    public ResponseEntity<?> unRegisterCourse(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId
    ) {
        return this.resSuccess(userService.cancelCourseByUserIdAndCourseId(userId, courseId));
    }


}
