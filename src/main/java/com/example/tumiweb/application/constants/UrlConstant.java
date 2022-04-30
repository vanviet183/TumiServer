package com.example.tumiweb.application.constants;

public class UrlConstant {

  public static class User {
    private User() {
    }

    private static final String PRE_FIX = "/users";
    public static final String DATA_USER = PRE_FIX;
    public static final String DATA_USER_ID = PRE_FIX + "/{id}";
    public static final String DATA_USER_SEARCH = PRE_FIX + "/search";
    public static final String DATA_USER_STATUS = PRE_FIX + "/{id}/status";
    public static final String DATA_USER_AVATAR = PRE_FIX + "/{id}/avatar";
    public static final String DATA_USER_EXPORT = PRE_FIX + "/export";
    public static final String DATA_USER_IMPORT = PRE_FIX + "/import";
  }

  public static class Role {
    private Role() {
    }

    private static final String PRE_FIX = "/roles";
    public static final String DATA_ROLE = PRE_FIX;
    public static final String DATA_ROLE_ID = PRE_FIX + "/{id}";
  }

  public static class Course {
    private Course() {
    }

    private static final String PRE_FIX = "/courses";
    public static final String DATA_COURSE = PRE_FIX;
    public static final String DATA_COURSE_ID = PRE_FIX + "/{id}";
    public static final String DATA_COURSE_CATEGORY_ID = PRE_FIX + "/{categoryId}";
    public static final String DATA_COURSE_IMAGE = PRE_FIX + "/image";
    public static final String DATA_COURSE_ID_STATUS = PRE_FIX + "/{id}/status";
    public static final String DATA_COURSE_CHANGE_CATEGORY = PRE_FIX + "/{courseId}/{categoryId}/category";
    public static final String DATA_COURSE_SEARCH = PRE_FIX + "/search/{key}";
  }

  public static class Chapter {
    private Chapter() {
    }

    private static final String PRE_FIX = "/chapters";
    public static final String DATA_CHAPTER = PRE_FIX;
    public static final String DATA_CHAPTER_ID = PRE_FIX + "/{id}";
    public static final String DATA_CHAPTER_COURSE_ID = PRE_FIX + "/{courseId}";
    public static final String DATA_CHAPTER_DETAIL = PRE_FIX + "/detail";
  }

  public static class Question {
    private Question() {
    }

    private static final String PRE_FIX = "/questions";
    public static final String DATA_QUESTION = PRE_FIX;
    public static final String DATA_QUESTION_ID = PRE_FIX + "/{id}";
    public static final String DATA_QUESTION_DETAIL = PRE_FIX + "detail";
    public static final String DATA_QUESTION_CHAPTER_ID = PRE_FIX + "/{chapterId}";
    public static final String DATA_QUESTION_EXPORT = PRE_FIX + "/export/{chapterId}";
    public static final String DATA_QUESTION_IMPORT = PRE_FIX + "/import";
  }

  public static class Answer {
    private Answer() {
    }

    private static final String PRE_FIX = "/answers";
    public static final String DATA_ANSWER = PRE_FIX;
    public static final String DATA_ANSWER_ALL = PRE_FIX + "/all";
    public static final String DATA_ANSWER_DETAIL = PRE_FIX + "/detail";
    public static final String DATA_ANSWER_WITH_ID = PRE_FIX + "/{id}";
    public static final String DATA_ANSWER_WITH_QUESTION_ID = PRE_FIX + "/{questionId}";

  }

  public static class Category {
    private Category() {
    }

    private static final String PRE_FIX = "/categories";
    public static final String DATA_CATEGORY = PRE_FIX;
    public static final String DATA_CATEGORY_ID = PRE_FIX + "/{id}";
    public static final String DATA_CATEGORY_STATUS = PRE_FIX + "/{id}/status";
  }

  public static class Gift {
    private Gift() {
    }

    private static final String PRE_FIX = "/gifts";
    public static final String DATA_GIFT = PRE_FIX;
    public static final String DATA_GIFT_ID = PRE_FIX + "/{id}";
    public static final String DATA_GIFT_SEARCH = PRE_FIX + "/search";
    public static final String DATA_GIFT_ID_STATUS = PRE_FIX + "/{id}/status";
    public static final String DATA_GIFT_ID_IMAGE = PRE_FIX + "/{id}/image";
  }

  public static class GiftOrder {
    private GiftOrder() {
    }

    private static final String PRE_FIX = "/gift-orders";
    public static final String DATA_GIFT_ORDER = PRE_FIX;
    public static final String DATA_GIFT_ORDER_ID = PRE_FIX + "/{id}";
    public static final String DATA_GIFT_ORDER_CREATE = PRE_FIX + "/{userId}/{giftId}";
  }

  public static class Help {
    private Help() {
    }

    private static final String PRE_FIX = "/helps";
    public static final String DATA_HELP = PRE_FIX;
    public static final String DATA_HELP_ID = PRE_FIX + "/{id}";
    public static final String DATA_HELP_USER_ID = PRE_FIX + "/{userId}";
    public static final String DATA_HELP_ID_STATUS = PRE_FIX + "/{id}/status";
  }

  public static class Notification {
    private Notification() {
    }

    private static final String PRE_FIX = "/notifications";
    public static final String DATA_NOTIFICATION = PRE_FIX;
    public static final String DATA_NOTIFICATION_ID = PRE_FIX + "/{id}";
    public static final String DATA_NOTIFICATION_ID_STATUS = PRE_FIX + "/{id}/status";
    public static final String DATA_NOTIFICATION_USER_ID = PRE_FIX + "/{userId}";
  }

  public static class Diary {
    private Diary() {
    }

    private static final String PRE_FIX = "/diaries";
    public static final String DATA_DIARY = PRE_FIX;
    public static final String DATA_DIARY_ID = PRE_FIX + "/{id}";
    public static final String DATA_DIARY_DETAIL = PRE_FIX + "/detail";
    public static final String DATA_DIARY_USER_ID = PRE_FIX + "/{userId}";
    public static final String DATA_DIARY_USER_ID_DAY = PRE_FIX + "/{userId}/{day}";
  }

  public static class DBFile {
    private DBFile() {
    }

    private static final String PRE_FIX = "/db-files";
  }

  public static class UserRole {
    private UserRole() {
    }

    private static final String PRE_FIX = "/user-role";
    public static final String DATA_USER_ROLE = PRE_FIX;
    public static final String DATA_USER_ROLE_UID = PRE_FIX + "/{userId}";
    public static final String DATA_USER_ROLE_ADD_ROLE = PRE_FIX + "/add/{userId}/{roleId}";
    public static final String DATA_USER_ROLE_REMOVE_ROLE = PRE_FIX + "/remove/{userId}/{roleId}";
  }

  public static class UserCourse {
    private UserCourse() {
    }

    private static final String PRE_FIX = "/user-course";
    public static final String DATA_USER_COURSE = PRE_FIX;
    public static final String DATA_USER_COURSE_REGIS = PRE_FIX + "/register/{userId}/{courseId}";
    public static final String DATA_USER_COURSE_UN_REGIS = PRE_FIX + "/unregister/{userId}/{courseId}";
    public static final String DATA_USER_COURSE_FIND = PRE_FIX + "/{userId}";
  }

  public static class Timer {
    private Timer() {
    }

    private static final String PRE_FIX = "/timer";
    public static final String DATA_TIMER = PRE_FIX;
    public static final String DATA_TIMER_NOW = PRE_FIX + "/{id}/now";
    public static final String DATA_TIMER_MONTH = PRE_FIX + "/{id}/{month}";
  }

  public static class Auth {
    private Auth() {
    }

    private static final String PRE_FIX = "/auth";
    public static final String LOGIN = PRE_FIX + "/login";
    public static final String SIGNUP = PRE_FIX + "/signup";
    public static final String VALIDATE = PRE_FIX + "/validate";
    public static final String LOGOUT = PRE_FIX + "/logout/{id}";
  }

}