package com.example.tumiweb.controller;


import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dto.CourseDTO;
import com.example.tumiweb.dao.Course;
import com.example.tumiweb.services.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController extends BaseController<Course> {

    @Autowired
    private ICourseService courseService;

    @GetMapping("")
    public ResponseEntity<?> getAllCourse(
            @RequestParam(name = "page", required = false) Long page,
            @RequestParam(name = "status", required = false) boolean status,
            @RequestParam(name = "both", required = false) boolean both
    ) {
        return this.resSetSuccess(courseService.findAllCourse(page, Constants.SIZE_OFF_PAGE, status, both));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable("id") Long id) {
        return this.resSuccess(courseService.findCourseById(id));
    }

    @PostMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<?> createNewCourse(
            @RequestBody CourseDTO courseDTO,
            @RequestParam(name = "image", required = false) MultipartFile image,
            @PathVariable("categoryId") Long categoryId) {
        return this.resSuccess(courseService.createNewCourse(courseDTO, image, categoryId));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<?> editCourseById(
            @PathVariable("id") Long id,
            @RequestBody CourseDTO courseDTO,
            @RequestParam(name = "image", required = false) MultipartFile image) {
        return this.resSuccess(courseService.editCourseById(id, courseDTO, image));
    }

    @PostMapping("/{id}/image")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<?> changeAvatarById(
            @PathVariable("id") Long id,
            @RequestParam(value = "image") MultipartFile image
    ) {
        return this.resSuccess(courseService.changeAvatarCourseById(id, image));
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<?> changeStatusCourseById(@PathVariable("id") Long id) {
        return this.resSuccess(courseService.changeStatusById(id));
    }

    @PostMapping("/{courseId}/{categoryId}/category")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<?> changeCategory(
            @PathVariable("courseId") Long courseId,
            @PathVariable("categoryId") Long categoryId
    ) {
        return this.resSuccess(courseService.editCategoryById(courseId, categoryId));
    }

}
