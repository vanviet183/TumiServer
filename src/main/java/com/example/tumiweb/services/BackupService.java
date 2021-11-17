package com.example.tumiweb.services;

import com.example.tumiweb.dao.*;
import com.example.tumiweb.excel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackupService {

    @Autowired private IUserService userService;

    @Autowired private ICategoryService categoryService;
    @Autowired private IQuestionService questionService;
    @Autowired private IAnswerService answerService;
    @Autowired private IDiaryService diaryService;
    @Autowired private IGiftService giftService;
    @Autowired private IHelpService helpService;
    @Autowired private INotificationService notificationService;
    @Autowired private ICourseService courseService;

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

    public boolean backupCategory(HttpServletResponse res) {
        try {
            res.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachement; filename=category.xlsx";

            res.setHeader(headerKey, headerValue);

            List<Category> categories = new ArrayList<>(categoryService.findAllCategory(null, 0, true, true));

            WriteExcelFileCategory writeExcelFileCategory = new WriteExcelFileCategory(categories);
            writeExcelFileCategory.export(res);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean backupChapter(HttpServletResponse res) {
        try {
            res.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachement; filename=chapter.xlsx";

            res.setHeader(headerKey, headerValue);

            List<Category> categories = new ArrayList<>(categoryService.findAllCategory(null, 0, true, true));

            WriteExcelFileCategory writeExcelFileCategory = new WriteExcelFileCategory(categories);
            writeExcelFileCategory.export(res);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean backupDiary(HttpServletResponse res) {
        try {
            res.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachement; filename=diary.xlsx";

            res.setHeader(headerKey, headerValue);

            List<Diary> diaries = diaryService.findAll();

            WriteExcelFileDiary writeExcelFileDiary = new WriteExcelFileDiary(diaries);
            writeExcelFileDiary.export(res);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean backupGift(HttpServletResponse res) {
        try {
            res.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachement; filename=gift.xlsx";

            res.setHeader(headerKey, headerValue);

            List<Gift> gifts = new ArrayList<>(giftService.getAllGift(null, 0, true));

            WriteExcelFileGift writeExcelFileGift = new WriteExcelFileGift(gifts);
            writeExcelFileGift.export(res);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean backupHelp(HttpServletResponse res) {
        try {
            res.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachement; filename=help.xlsx";

            res.setHeader(headerKey, headerValue);

            List<Help> helps = new ArrayList<>(helpService.getAllHelp(null, 0));

            WriteExcelFileHelp writeExcelFileHelp = new WriteExcelFileHelp(helps);
            writeExcelFileHelp.export(res);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean backupNotification(HttpServletResponse res) {
        try {
            res.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachement; filename=notification.xlsx";

            res.setHeader(headerKey, headerValue);

            List<Notification> notifications = new ArrayList<>(notificationService.getAllNotification(null, 0, true));

            WriteExcelFileNotification writeExcelFileNotification = new WriteExcelFileNotification(notifications);
            writeExcelFileNotification.export(res);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean backupCourse(HttpServletResponse res) {
        try {
            res.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachement; filename=course.xlsx";

            res.setHeader(headerKey, headerValue);

            List<Course> courses = new ArrayList<>(courseService.findAllCourse(null, 0, true, true));

            WriteExcelFileCourse writeExcelFileCourse = new WriteExcelFileCourse(courses);
            writeExcelFileCourse.export(res);

            return true;
        } catch (Exception e) {
            return false;
        }
    }




}
