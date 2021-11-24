package com.example.tumiweb.services.imp;

import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.services.IDiaryService;
import com.example.tumiweb.services.ISendMailService;
import com.example.tumiweb.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class SendMailServiceImp implements ISendMailService {

    @Autowired private JavaMailSender javaMailSender;
    @Autowired private IUserService userService;
    @Autowired private IDiaryService diaryService;
    SimpleDateFormat formatDay = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public String sendMailWithText(String sub, String content, String to) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setSubject(sub);
            simpleMailMessage.setText(content);
            simpleMailMessage.setTo(to);

            javaMailSender.send(simpleMailMessage);
        }catch (Exception e) {
            return "Send failed";
        }

        return "Send successfully";
    }

    @Override
    public String sendMailWithAtt(String sub, String content, String to, MultipartFile[] att) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(sub);
            helper.setText(content);

            if(att != null && att.length > 0) {
                for(MultipartFile multipartFile : att) {
                    helper.addAttachment(multipartFile.getName(), multipartFile);
                }
            }

            javaMailSender.send(message);

        }catch (Exception e) {
            return "Send failed";
        }
        return "Send successfully";
    }

    @Override
    public void sendMailToAdmin() {
        List<User> admins = userService.getAllUserAdmin();
        admins.forEach(admin -> sendMailWithText(Constants.SUBJECT_ERROR_BACKUP_DATA, Constants.CONTENT_ERROR_BACKUP_DATA, admin.getEmail()));
    }

    @Override
    public void sendMailToUserForBirthday(List<User> users) {
        users.forEach(user -> sendMailWithText(
                Constants.SUBJECT_BIRTHDAY,
                "Chúc mừng sinh nhật " + user.getFullName() + ". Chúc bạn có một sinh nhật vui vẻ <3",
                user.getEmail()
        ));
    }

    @Override
    public void sendMailToUserCallLearn() {
        Set<User> users = diaryService.findAllUserByDay(formatDay.format(new Date()));
        users.forEach(user -> {
            System.out.println(user.getEmail());
            sendMailWithText(Constants.SUBJECT_CALL_LEARN, Constants.CONTENT_CALL_LEARN, user.getEmail());
        });
    }
}
