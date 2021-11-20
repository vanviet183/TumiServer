package com.example.tumiweb.constants;

import org.springframework.beans.factory.annotation.Value;

public class Constants {
    public static int SIZE_OFF_PAGE = 10;

//    @Value("${email.username}")
    public static String MY_EMAIL = "bucuthangban12@gmail.com";
//    @Value("${email.password}")
    public static String MY_PASSWORD = "eswevrssoinmcljl";

    public static String SUBJECT_REGISTER = "Đăng ký tài khoản tại Tumi";

    public static String SUBJECT_CALL_LEARN = "Tumi";
    public static String CONTENT_CALL_LEARN = "Vào học đi nào, lười thế !!!";

    public static String SUBJECT_FORGOT_PASS = "Quên mật khẩu Tumi web";

    public static String SUBJECT_ERROR_BACKUP_DATA = "Lỗi backup data";
    public static String CONTENT_ERROR_BACKUP_DATA = "Backup data lỗi, yêu cầu bảo trì hệ thống!!!";

    public static String PATH_USER_FILE = "data-backup/users.xlsx";
    public static String PATH_HELP_FILE = "data-backup/helps.xlsx";
    public static String PATH_GIFT_FILE = "data-backup/gifts.xlsx";
    public static String PATH_NOTIFICATION_FILE = "data-backup/notifications.xlsx";
    public static String PATH_DIARY_FILE = "data-backup/diaries.xlsx";
    public static String PATH_COURSE_FILE = "data-backup/courses.xlsx";
    public static String PATH_CHAPTER_FILE = "data-backup/chapters.xlsx";
    public static String PATH_QUESTION_FILE = "data-backup/question.xlsx";
    public static String PATH_ANSWER_FILE = "data-backup/answer.xlsx";
    public static String PATH_CATEGORY_FILE = "data-backup/categories.xlsx";

}
