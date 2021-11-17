package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.dto.QuestionDTO;
import com.example.tumiweb.dao.Question;
import com.example.tumiweb.excel.ReadExcelFile;
import com.example.tumiweb.excel.WriteExcelFileQuestion;
import com.example.tumiweb.excel.WriteExcelFileUser;
import com.example.tumiweb.services.IQuestionService;
import com.example.tumiweb.utils.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<?> createNewQuestion(
            @PathVariable("chapterId") Long id,
            @RequestBody QuestionDTO questionDTO,
            @RequestParam(name = "image", required = false) MultipartFile image){
        return this.resSuccess(questionService.createNewQuestion(questionDTO, id, image));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<?> editQuestionById(
            @PathVariable Long id,
            @RequestBody QuestionDTO questionDTO,
            @RequestParam(name = "image", required = false) MultipartFile image
    ) {
        return this.resSuccess(questionService.editQuestionById(id, questionDTO, image));
    }

    // Excel file
    @GetMapping("/export/{chapterId}")
    public ResponseEntity<?> exportToExcel(HttpServletResponse res, @PathVariable("chapterId") Long chapterId) throws IOException {
        res.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachement; filename=questions" + "-IDChapter" + chapterId + ".xlsx";

        res.setHeader(headerKey, headerValue);

        //data
        List<Question> questions = new ArrayList<>(questionService.findAllQuestionByChapterId(chapterId, null, 0));

        WriteExcelFileQuestion writeExcelFileQuestion = new WriteExcelFileQuestion(questions);
        writeExcelFileQuestion.export(res);

        return ResponseEntity.ok("Export success");
    }

    @PostMapping("/import")
    public ResponseEntity<?> importExcelFile(
            @RequestParam(name = "file") MultipartFile file
    ) throws IOException {
        List<Question> questions = ReadExcelFile.readFileQuestion(UploadFile.convertMultipartToFile(file));

        //Do something with users

        return ResponseEntity.status(200).body(questions);
    }

}
