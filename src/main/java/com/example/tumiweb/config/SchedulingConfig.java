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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
        if(!backupService.backupUser(new Response())) {
            //this is failed
            //Send notification to admin
            System.out.println("failed");
        }

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
                    //Send notification to admin
                    e.printStackTrace();
                }
            });
        });

        //This is code backup answers
        if(!backupService.backupAnswer(new Response())) {
            //this is failed
            //Send notification to admin
            System.out.println("failed");
        }

    }


}
