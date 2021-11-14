package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.dao.Diary;
import com.example.tumiweb.services.IDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/diaries")
public class DiaryController extends BaseController<Diary> {

    @Autowired
    private IDiaryService diaryService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> findAllDiaryByUserId(@PathVariable("userId") Long id) {
        return this.resSetSuccess(diaryService.findAllByUserId(id));
    }

    @GetMapping("/{userId}/{day}")
    public ResponseEntity<?> findAllDiaryByUserIdOneDay(
            @PathVariable("userId") Long userId,
            @PathVariable("day") String day
    ) {
        return this.resListSuccess(diaryService.findAllByUserIdAndOnDay(userId, day));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        return this.resSuccess(diaryService.findDiaryById(id));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> createNewDiary(@PathVariable("userId") Long id) {
        return this.resSuccess(diaryService.createNewDiary(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editDiaryById(
            @PathVariable("id") Long id
    ) {
        return this.resSuccess(diaryService.editDiaryById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiaryById(@PathVariable("id") Long id) {
        return this.resSuccess(diaryService.deleteDiaryById(id));
    }

}
