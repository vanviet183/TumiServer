package com.example.tumiweb.services;

import com.example.tumiweb.dao.Answer;
import com.example.tumiweb.dao.Question;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.excel.WriteExcelFileAnswer;
import com.example.tumiweb.excel.WriteExcelFileQuestion;
import com.example.tumiweb.excel.WriteExcelFileUser;
import com.example.tumiweb.services.IQuestionService;
import com.example.tumiweb.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackupService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IQuestionService questionService;
    @Autowired private IAnswerService answerService;

    public boolean backupUser(HttpServletResponse res) throws IOException {
        try {
            res.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachement; filename=users.xlsx";

            res.setHeader(headerKey, headerValue);

            //data
            List<User> users = new ArrayList<>(userService.getAllUsers(null, 0, false, false));

            WriteExcelFileUser userExcelService = new WriteExcelFileUser(users);
            userExcelService.export(res);

            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean backupQuestionByChapterId(HttpServletResponse res, Long chapterId) throws IOException {
        try {
            res.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachement; filename=questions" + "-IDChapter" + chapterId + ".xlsx";

            res.setHeader(headerKey, headerValue);

            //data
            List<Question> questions = new ArrayList<>(questionService.findAllQuestionByChapterId(chapterId, null, 0));

            WriteExcelFileQuestion writeExcelFileQuestion = new WriteExcelFileQuestion(questions);
            writeExcelFileQuestion.export(res);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean backupAnswer(HttpServletResponse res) {
        try {
            res.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachement; filename=answers.xlsx";

            res.setHeader(headerKey, headerValue);

            //data
            List<Answer> answers = new ArrayList<>(answerService.findAllAnswer());

            WriteExcelFileAnswer writeExcelFileAnswer = new WriteExcelFileAnswer(answers);
            writeExcelFileAnswer.export(res);

            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
