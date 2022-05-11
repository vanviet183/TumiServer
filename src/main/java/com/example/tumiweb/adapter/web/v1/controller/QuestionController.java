package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.CommonConstant;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.excel.ReadExcelFile;
import com.example.tumiweb.application.services.IQuestionService;
import com.example.tumiweb.application.services.imp.BackupServiceImp;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.domain.dto.QuestionDTO;
import com.example.tumiweb.domain.entity.Question;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestApiV1
public class QuestionController {
  private final IQuestionService questionService;
  private final BackupServiceImp backupService;

  public QuestionController(IQuestionService questionService, BackupServiceImp backupService) {
    this.questionService = questionService;
    this.backupService = backupService;
  }

  @GetMapping(UrlConstant.Question.DATA_QUESTION_CHAPTER_ID)
  public ResponseEntity<?> getAllQuestionByChapterId(@PathVariable("chapterId") Long chapterId,
                                                     @RequestParam(name = "page", required = false) Long page) {
    return VsResponseUtil.ok(questionService.findAllQuestionByChapterId(chapterId, page, CommonConstant.SIZE_OFF_PAGE));
  }

  @GetMapping(UrlConstant.Question.DATA_QUESTION_DETAIL)
  public ResponseEntity<?> getQuestionById(@RequestParam("id") Long id) {
    return VsResponseUtil.ok(questionService.findQuestionById(id));
  }

  @PostMapping(UrlConstant.Question.DATA_QUESTION_CHAPTER_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> createNewQuestion(@PathVariable("chapterId") Long id, @RequestBody QuestionDTO questionDTO,
                                             @RequestParam(name = "image", required = false) MultipartFile image) {
    return VsResponseUtil.ok(questionService.createNewQuestion(questionDTO, id, image));
  }

  @PatchMapping(UrlConstant.Question.DATA_QUESTION_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> editQuestionById(@PathVariable Long id, @RequestBody QuestionDTO questionDTO,
                                            @RequestParam(name = "image", required = false) MultipartFile image) {
    return VsResponseUtil.ok(questionService.editQuestionById(id, questionDTO, image));
  }

  @DeleteMapping(UrlConstant.Question.DATA_QUESTION_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> deleteQuestionById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(questionService.deleteQuestionById(id));
  }

  // Excel file
  @GetMapping(UrlConstant.Question.DATA_QUESTION_EXPORT)
  public ResponseEntity<?> exportToExcel(HttpServletResponse res, @PathVariable("chapterId") Long chapterId) throws IOException {
    if (!backupService.backupQuestionByChapterId(res, chapterId)) {
      //send notification to admin

      return ResponseEntity.ok("Export failed");
    }

    return ResponseEntity.ok("Export success");
  }

  @PostMapping(UrlConstant.Question.DATA_QUESTION_IMPORT)
  public ResponseEntity<?> importExcelFile(@RequestParam(name = "file") MultipartFile file) throws IOException {
    List<Question> questions = ReadExcelFile.readFileQuestion(UploadFile.convertMultipartToFile(file));

    //Do something with question

    return ResponseEntity.status(200).body(questions);
  }

}
