package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dto.ChapterDTO;
import com.example.tumiweb.dao.Chapter;
import com.example.tumiweb.services.IChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chapters")
public class ChapterController extends BaseController<Chapter> {

    @Autowired
    private IChapterService chapterService;

    @GetMapping("/{courseId}")
    public ResponseEntity<?> getAllChapterByIdCourse(
            @PathVariable("courseId") Long id,
            @RequestParam(name = "page", required = false) Long page,
            @RequestParam(name = "status", required = false) boolean status,
            @RequestParam(name = "both", required = false) boolean both
    ) {
        return this.resSetSuccess(chapterService.findAllChapterByCourseId(id, page, Constants.SIZE_OFF_PAGE, status, both));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<?> getChapterById(@PathVariable("id") Long id) {
        return this.resSuccess(chapterService.findChapterById(id));
    }

    @PostMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<?> createNewChapter(
            @PathVariable("courseId") Long id,
            @RequestBody ChapterDTO chapterDTO) {
        return this.resSuccess(chapterService.createNewChapter(chapterDTO, id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<?> editCourseById(
            @PathVariable("id") Long id,
            @RequestBody ChapterDTO chapterDTO
    ) {
        return this.resSuccess(chapterService.editChapterById(id, chapterDTO));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteChapterById(@PathVariable("id") Long id) {
//        return this.resSuccess(chapterService.deleteChapterById(id));
//    }

}
