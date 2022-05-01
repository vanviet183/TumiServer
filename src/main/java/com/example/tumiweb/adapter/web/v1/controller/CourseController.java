package com.example.tumiweb.adapter.web.v1.controller;


import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.EmailConstant;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.ICourseService;
import com.example.tumiweb.domain.dto.CourseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestApiV1
public class CourseController {
  private final ICourseService courseService;

  public CourseController(ICourseService courseService) {
    this.courseService = courseService;
  }

  @GetMapping(UrlConstant.Course.DATA_COURSE)
  public ResponseEntity<?> getAllCourse(@RequestParam(name = "page", required = false) Long page,
                                        @RequestParam(name = "status", required = false) boolean status,
                                        @RequestParam(name = "both", required = false) boolean both) {
    return VsResponseUtil.ok(courseService.findAllCourse(page, EmailConstant.SIZE_OFF_PAGE, status, both));
  }

  @GetMapping(UrlConstant.Course.DATA_COURSE_ID)
  public ResponseEntity<?> getCourseById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(courseService.findCourseById(id));
  }

  @PostMapping(UrlConstant.Course.DATA_COURSE_CATEGORY_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> createNewCourse(@RequestBody CourseDTO courseDTO,
                                           @RequestParam(name = "image", required = false) MultipartFile image,
                                           @PathVariable("categoryId") Long categoryId) {
    return VsResponseUtil.ok(courseService.createNewCourse(courseDTO, image, categoryId));
  }

  @PatchMapping(UrlConstant.Course.DATA_COURSE_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> editCourseById(@PathVariable("id") Long id, @RequestBody CourseDTO courseDTO,
                                          @RequestParam(name = "image", required = false) MultipartFile image) {
    return VsResponseUtil.ok(courseService.editCourseById(id, courseDTO, image));
  }

  @PostMapping(UrlConstant.Course.DATA_COURSE_IMAGE)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> changeAvatarById(@RequestParam("id") Long id,
                                            @RequestParam(value = "image") MultipartFile image) {
    return VsResponseUtil.ok(courseService.changeAvatarCourseById(id, image));
  }

  @PostMapping(UrlConstant.Course.DATA_COURSE_ID_STATUS)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> changeStatusCourseById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(courseService.changeDeleteFlagById(id));
  }

  @PostMapping(UrlConstant.Course.DATA_COURSE_CHANGE_CATEGORY)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> changeCategory(@PathVariable("courseId") Long courseId,
                                          @PathVariable("categoryId") Long categoryId) {
    return VsResponseUtil.ok(courseService.editCategoryById(courseId, categoryId));
  }

  @GetMapping(UrlConstant.Course.DATA_COURSE_SEARCH)
  public ResponseEntity<?> searchCoursesByKey(@PathVariable("key") String key) {
    return VsResponseUtil.ok(courseService.getCoursesByKey(key));
  }

}
