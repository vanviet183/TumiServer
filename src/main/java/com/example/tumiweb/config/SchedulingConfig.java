package com.example.tumiweb.config;

import com.example.tumiweb.dao.*;
import com.example.tumiweb.services.*;
import com.example.tumiweb.services.imp.BackupServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@EnableAsync
@Configuration
@Component
@EnableScheduling
@ConditionalOnExpression("true")
public class SchedulingConfig {

    @Autowired
    private IDiaryService diaryService;

    @Autowired
    private ISendMailService sendMailService;

    @Autowired
    private BackupServiceImp backupService;

    @Autowired private ICourseService courseService;
    @Autowired private IChapterService chapterService;

    @Async
    @Scheduled(cron = "0 47 0 ? * MON-SAT")
//    @Scheduled(cron = "0 * * ? * *")
    void sendMailToUserCallLearn() {
        sendMailService.sendMailToUserCallLearn();
    }

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    void sendMailToUserForBirthday() {
        sendMailService.sendMailToUserForBirthday();
    }


    @Async
    @Scheduled(cron = "0 18 15 20 * ?")
//    @Scheduled(cron = "0 0 0 15 * ?")
    void backupData() {
        //backup user
        boolean isSuccessUser = backupService.backupUser(null);
        System.out.println("User: " + isSuccessUser);
        //This is code backup helps
        boolean isSuccessHelp = backupService.backupHelp(null);
        System.out.println("Help: " + isSuccessHelp);
        //This is code backup diary
        boolean isSuccessDiary = backupService.backupDiary(null);
        System.out.println("Diary: " + isSuccessDiary);
        //This is code backup notification
        boolean isSuccessNotification = backupService.backupNotification(null);
        System.out.println("Notification: " + isSuccessNotification);
        //This is code backup gift
        boolean isSuccessGift = backupService.backupGift(null);
        System.out.println("Gift: " + isSuccessGift);
        //This is code backup course
        boolean isSuccessCourse = backupService.backupCourse(null);
        System.out.println("Course: " + isSuccessCourse);
        //This is code backup chapter
        boolean isSuccessChapter = backupService.backupChapter(null);
        System.out.println("Chapter: " + isSuccessChapter);
        //This is code backup category
        boolean isSuccessCategory = backupService.backupCategory(null);
        System.out.println("Category: " + isSuccessCategory);
        //This is code backup answers
        boolean isSuccessAnswer = backupService.backupAnswer(null);
        System.out.println("Answer: " + isSuccessAnswer);
        AtomicBoolean isSuccessQuestion = new AtomicBoolean(true);

        //backup question
        //find all course
        List<Course> courses = new ArrayList<>(courseService.findAllCourse(null, 0, true, true));
        //This is code backup Courses
        courses.forEach(course -> {
            //This is code backup chapters
            List<Chapter> chapters = new ArrayList<>(chapterService.findAllChapter(null, 0));
            chapters.forEach(chapter -> {
                //This is code backup questions
                try {
                    backupService.backupQuestionByChapterId(null, chapter.getId());
                    System.out.println("question");
                } catch (IOException e) {
                    isSuccessQuestion.set(false);
                }
            });
        });


        //checking
        if(     !isSuccessUser ||
                !isSuccessQuestion.get() ||
                !isSuccessAnswer ||
                !isSuccessCourse ||
                !isSuccessChapter ||
                !isSuccessHelp ||
                !isSuccessGift ||
                !isSuccessCategory ||
                !isSuccessNotification
        ) {
            sendMailService.sendMailToAdmin();
        }
    }


}
