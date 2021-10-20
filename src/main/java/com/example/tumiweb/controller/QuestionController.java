package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dto.QuestionDTO;
import com.example.tumiweb.model.Question;
import com.example.tumiweb.services.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController extends BaseController<Question> {

    @Autowired
    private IQuestionService questionService;

    @GetMapping("/{chapterId}")
    public ResponseEntity<?> getAllQuestionByChapterId(
            @PathVariable("chapterId") Long chapterId,
            @RequestParam(name = "page", required = false) Long page
    ) {
        return this.resSetSuccess(questionService.findAllQuestionByChapterId(chapterId, page, Constants.SIZE_OFF_PAGE));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<?> getQuestionById(@PathVariable("id") Long id) {
        return this.resSuccess(questionService.findQuestionById(id));
    }

    @PostMapping("/{chapterId}")
    public ResponseEntity<?> createNewQuestion(
            @PathVariable("chapterId") Long id,
            @RequestBody QuestionDTO questionDTO,
            @RequestParam(name = "image", required = false) MultipartFile image){
        return this.resSuccess(questionService.createNewQuestion(questionDTO, id, image));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editQuestionById(
            @PathVariable Long id,
            @RequestBody QuestionDTO questionDTO,
            @RequestParam(name = "image", required = false) MultipartFile image
    ) {
        return this.resSuccess(questionService.editQuestionById(id, questionDTO, image));
    }

}
