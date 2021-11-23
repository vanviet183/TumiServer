package com.example.tumiweb.services;

import org.springframework.web.multipart.MultipartFile;

public interface ISendMailService {
    String sendMailWithText(String sub, String content, String to);
    String sendMailWithAtt(String sub, String content, String to, MultipartFile[] att);

    void sendMailToAdmin();
    void sendMailToUserForBirthday();
    void sendMailToUserCallLearn();
//    String sendMailWithHTML();//rảnh thì làm thêm
}
