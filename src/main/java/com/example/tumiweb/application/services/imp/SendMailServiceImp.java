package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.constants.EmailConstant;
import com.example.tumiweb.application.dai.DiaryRepository;
import com.example.tumiweb.application.dai.RoleRepository;
import com.example.tumiweb.application.dai.UserRepository;
import com.example.tumiweb.application.services.ISendMailService;
import com.example.tumiweb.application.utils.DateTimeUtil;
import com.example.tumiweb.domain.entity.Diary;
import com.example.tumiweb.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SendMailServiceImp implements ISendMailService {
  @Autowired
  private JavaMailSender javaMailSender;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private DiaryRepository diaryRepository;
  @Autowired
  private RoleRepository roleRepository;

  @Override
  public String sendMailWithText(String sub, String content, String to) {
    try {
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

      simpleMailMessage.setSubject(sub);
      simpleMailMessage.setText(content);
      simpleMailMessage.setTo(to);

      javaMailSender.send(simpleMailMessage);
    } catch (Exception e) {
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

      if (att != null && att.length > 0) {
        for (MultipartFile multipartFile : att) {
          helper.addAttachment(multipartFile.getName(), multipartFile);
        }
      }

      javaMailSender.send(message);

    } catch (Exception e) {
      return "Send failed";
    }
    return "Send successfully";
  }

  @Override
  public void sendMailToAdmin() {
    List<User> admins =
        userRepository.findAll().parallelStream().filter(item -> item.getRoles().contains(roleRepository.findByName(
            "ROLE_ADMIN"))).collect(Collectors.toList());
    admins.forEach(admin -> sendMailWithText(EmailConstant.SUBJECT_ERROR_BACKUP_DATA,
        EmailConstant.CONTENT_ERROR_BACKUP_DATA, admin.getEmail())
    );
  }

  @Override
  public void sendMailToUserForBirthday(List<User> users) {
    users.forEach(user -> sendMailWithText(
        EmailConstant.SUBJECT_BIRTHDAY,
        "Chúc mừng sinh nhật " + user.getFullName() + ". Chúc bạn có một sinh nhật vui vẻ <3",
        user.getEmail()
    ));
  }

  @Override
  public void sendMailToUserCallLearn() {
    List<User> users =
        diaryRepository.findAllByDay(DateTimeUtil.getDateTimeNow()).stream().map(Diary::getUser).collect(Collectors.toList());
    users.forEach(user -> {
      System.out.println(user.getEmail());
      sendMailWithText(EmailConstant.SUBJECT_CALL_LEARN, EmailConstant.CONTENT_CALL_LEARN, user.getEmail());
    });
  }

}
