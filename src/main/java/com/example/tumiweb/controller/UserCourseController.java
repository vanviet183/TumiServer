package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.services.ICourseService;
import com.example.tumiweb.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user-course")
public class UserCourseController extends BaseController<String> {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICourseService courseService;

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


    @GetMapping("/{userId}")
    public ResponseEntity<?> findCourseByUserId(
            @PathVariable("userId") Long id,
            @RequestParam(name = "status", required = false) boolean status,
            @RequestParam(name = "both", required = false) boolean both
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAllCourseByUserId(id, status, both));
    }


}
