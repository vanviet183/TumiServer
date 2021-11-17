package com.example.tumiweb.config;

import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dao.*;
import com.example.tumiweb.services.*;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
    private BackupService backupService;

    @Autowired private ICourseService courseService;
    @Autowired private IChapterService chapterService;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Async
    @Scheduled(cron = "0 30 20 ? * MON-SAT")
//    @Scheduled(cron = "0 * * ? * *")
    void sendMailToUser() {
        Set<User> users = diaryService.findAllUserByDay(simpleDateFormat.format(new Date()));
        users.forEach(user -> {
            System.out.println(user.getEmail());
            sendMailService.sendMailWithText(Constants.SUBJECT_CALL_LEARN, Constants.CONTENT_CALL_LEARN, user.getEmail());
        });
    }

    @Async
    @Scheduled(cron = "0 0 0 15 * ?")
    void backupData() throws IOException {
        //backup user
        boolean isSuccessUser = backupService.backupUser(new Response());
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
                    backupService.backupQuestionByChapterId(new Response(), chapter.getId());
                } catch (IOException e) {
                    isSuccessQuestion.set(false);
                }
            });
        });

        //This is code backup answers
        boolean isSuccessAnswer = backupService.backupAnswer(new Response());


        //checking
        if(!isSuccessUser || !isSuccessQuestion.get() || !isSuccessAnswer) {
            sendMailService.sendMailToAdmin();
        }
    }


}
