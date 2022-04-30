package com.example.tumiweb.application.constants;

import java.text.SimpleDateFormat;

public class CommonConstant {

  public static final String FORMAT_DATE_PATTERN = "dd/MM/yyyy";
  public static final String FORMAT_DATE_PATTERN_DETAIL = "dd/MM/yyyy HH:mm:ss";
  public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat(FORMAT_DATE_PATTERN);
  public static final SimpleDateFormat FORMAT_DATE_DETAIL = new SimpleDateFormat(FORMAT_DATE_PATTERN_DETAIL);

  public static final String COLUMN_ID = "id";
  public static final String COLUMN_USER_ID = "users_id";
  public static final String COLUMN_COURSE_ID = "course_id";
  public static final String COLUMN_ROLE_ID = "role_id";

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
