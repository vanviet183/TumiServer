package com.example.tumiweb.config;

import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.services.IDiaryService;
import com.example.tumiweb.services.ISendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
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


}
