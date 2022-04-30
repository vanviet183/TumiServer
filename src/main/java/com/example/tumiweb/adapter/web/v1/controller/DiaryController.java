package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.IDiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
public class DiaryController {
  private final IDiaryService diaryService;

  public DiaryController(IDiaryService diaryService) {
    this.diaryService = diaryService;
  }

  @GetMapping(UrlConstant.Diary.DATA_DIARY)
  public ResponseEntity<?> getAll() {
    return VsResponseUtil.ok(diaryService.findAll());
  }

  @GetMapping(UrlConstant.Diary.DATA_DIARY_USER_ID)
  public ResponseEntity<?> findAllDiaryByUserId(@PathVariable("userId") Long id) {
    return VsResponseUtil.ok(diaryService.findAllByUserId(id));
  }

  @GetMapping(UrlConstant.Diary.DATA_DIARY_USER_ID_DAY)
  public ResponseEntity<?> findAllDiaryByUserIdOneDay(@PathVariable("userId") Long userId,
                                                      @PathVariable("day") String day) {
    return VsResponseUtil.ok(diaryService.findAllByUserIdAndOnDay(userId, day));
  }

  @GetMapping(UrlConstant.Diary.DATA_DIARY_DETAIL)
  public ResponseEntity<?> findById(@RequestParam("id") Long id) {
    return VsResponseUtil.ok(diaryService.findDiaryById(id));
  }

  @PostMapping(UrlConstant.Diary.DATA_DIARY_USER_ID)
  public ResponseEntity<?> createNewDiary(@PathVariable("userId") Long id) {
    return VsResponseUtil.ok(diaryService.createNewDiary(id));
  }

  @PatchMapping(UrlConstant.Diary.DATA_DIARY_ID)
  public ResponseEntity<?> editDiaryById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(diaryService.editDiaryById(id));
  }

  @DeleteMapping(UrlConstant.Diary.DATA_DIARY)
  public ResponseEntity<?> deleteDiaryById(@RequestParam("id") Long id) {
    return VsResponseUtil.ok(diaryService.deleteDiaryById(id));
  }

}
