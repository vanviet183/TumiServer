package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.dto.AnswerDTO;
import com.example.tumiweb.model.Answer;
import com.example.tumiweb.services.IAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/answers")
public class AnswerController extends BaseController<Answer> {

    @Autowired
    private IAnswerService answerService;

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getAnswersByQuestionId(@PathVariable("questionId") Long questionId) {
        return this.resSetSuccess(answerService.findAllAnswerByQuestionId(questionId));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<?> getAnswerById(@PathVariable("id") Long id) {
        return this.resSuccess(answerService.findAnswerById(id));
    }

    @PostMapping("/{questionId}")
    public ResponseEntity<?> createAnswer(
            @PathVariable("questionId") Long questionId,
            @RequestBody AnswerDTO answerDTO,
            @RequestParam(name = "image", required = false) MultipartFile image
            ) {
        return this.resSuccess(answerService.createNewAnswer(answerDTO, questionId, image));
    }

    @PostMapping("/{questionId}/all")
    public ResponseEntity<?> createAnswers(
            @PathVariable("questionId") Long questionId,
            @RequestBody List<AnswerDTO> answerDTOS,
            @RequestParam(name = "images", required = false) MultipartFile[] images
            ){
        return this.resSetSuccess(answerService.createNewAnswers(questionId, answerDTOS, images));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editAnswerById(
            @PathVariable("id") Long id,
            @RequestBody AnswerDTO answerDTO,
            @RequestParam(name = "image", required = false) MultipartFile image) {
        return this.resSuccess(answerService.editAnswerById(id, answerDTO, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestionById(@PathVariable("id") Long id) {
        return this.resSuccess(answerService.deleteAnswerById(id));
    }

}
