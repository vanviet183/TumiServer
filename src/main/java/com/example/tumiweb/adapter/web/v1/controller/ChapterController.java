package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.EmailConstant;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.IChapterService;
import com.example.tumiweb.domain.dto.ChapterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
public class ChapterController {
  private final IChapterService chapterService;

  public ChapterController(IChapterService chapterService) {
    this.chapterService = chapterService;
  }

  @GetMapping(UrlConstant.Chapter.DATA_CHAPTER_COURSE_ID)
  public ResponseEntity<?> getAllChapterByIdCourse(@PathVariable("courseId") Long id,
                                                   @RequestParam(name = "page", required = false) Long page,
                                                   @RequestParam(name = "status", required = false) boolean status,
                                                   @RequestParam(name = "both", required = false) boolean both) {
    return VsResponseUtil.ok(chapterService.findAllChapterByCourseId(id, page, EmailConstant.SIZE_OFF_PAGE, status,
        both));
  }

  @GetMapping(UrlConstant.Chapter.DATA_CHAPTER_DETAIL)
  public ResponseEntity<?> getChapterById(@RequestParam("id") Long id) {
    return VsResponseUtil.ok(chapterService.findChapterById(id));
  }

  @PostMapping(UrlConstant.Chapter.DATA_CHAPTER_COURSE_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> createNewChapter(@PathVariable("courseId") Long id, @RequestBody ChapterDTO chapterDTO) {
    return VsResponseUtil.ok(chapterService.createNewChapter(chapterDTO, id));
  }

  @PatchMapping(UrlConstant.Chapter.DATA_CHAPTER_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> editCourseById(@PathVariable("id") Long id, @RequestBody ChapterDTO chapterDTO) {
    return VsResponseUtil.ok(chapterService.editChapterById(id, chapterDTO));
  }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteChapterById(@PathVariable("id") Long id) {
//        return VsResponseUtil.ok(chapterService.deleteChapterById(id));
//    }

}
