package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.IAnswerService;
import com.example.tumiweb.domain.dto.AnswerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestApiV1
public class AnswerController {
  private final IAnswerService answerService;

  public AnswerController(IAnswerService answerService) {
    this.answerService = answerService;
  }

  @GetMapping(UrlConstant.Answer.DATA_ANSWER)
  public ResponseEntity<?> getAnswersByQuestionId(@RequestParam("questionId") Long questionId) {
    return VsResponseUtil.ok(answerService.findAllAnswerByQuestionId(questionId));
  }

  @GetMapping(UrlConstant.Answer.DATA_ANSWER_DETAIL)
  public ResponseEntity<?> getAnswerById(@RequestParam("id") Long id) {
    return VsResponseUtil.ok(answerService.findAnswerById(id));
  }

  @PostMapping(UrlConstant.Answer.DATA_ANSWER_WITH_QUESTION_ID)
  public ResponseEntity<?> createAnswer(@PathVariable("questionId") Long questionId, @RequestBody AnswerDTO answerDTO,
                                        @RequestParam(name = "image", required = false) MultipartFile image) {
    return VsResponseUtil.ok(answerService.createNewAnswer(answerDTO, questionId, image));
  }

  @PostMapping(UrlConstant.Answer.DATA_ANSWER_ALL)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> createAnswers(@RequestBody List<AnswerDTO> answerDTOS,
                                         @RequestParam("questionId") Long questionId,
                                         @RequestParam(name = "images", required = false) MultipartFile[] images) {
    return VsResponseUtil.ok(answerService.createNewAnswers(questionId, answerDTOS, images));
  }

  @PatchMapping(UrlConstant.Answer.DATA_ANSWER_WITH_ID)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> editAnswerById(@PathVariable("id") Long id, @RequestBody AnswerDTO answerDTO,
                                          @RequestParam(name = "image", required = false) MultipartFile image) {
    return VsResponseUtil.ok(answerService.editAnswerById(id, answerDTO, image));
  }

  @DeleteMapping(UrlConstant.Answer.DATA_ANSWER)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> deleteQuestionById(@RequestParam("id") Long id) {
    return VsResponseUtil.ok(answerService.deleteAnswerById(id));
  }

}
