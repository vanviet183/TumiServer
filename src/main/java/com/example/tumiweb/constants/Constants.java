package com.example.tumiweb.constants;

import org.springframework.beans.factory.annotation.Value;

public class Constants {
    public static int SIZE_OFF_PAGE = 10;

    @Value("${email.username}")
    public static String MY_EMAIL;
    @Value("${email.password}")
    public static String MY_PASSWORD;

    public static String SUBJECT_REGISTER = "Đăng ký tài khoản tại Tumi";

    public static String SUBJECT_CALL_LEARN = "Tumi";
    public static String CONTENT_CALL_LEARN = "Vào học đi nào, lười thế !!!";
}
