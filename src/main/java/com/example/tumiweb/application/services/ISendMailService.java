package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISendMailService {

  String sendMailWithText(String sub, String content, String to);

  String sendMailWithAtt(String sub, String content, String to, MultipartFile[] att);

  void sendMailToAdmin();

  void sendMailToUserForBirthday(List<User> users);

  void sendMailToUserCallLearn();
//    String sendMailWithHTML();//rảnh thì làm thêm

}
