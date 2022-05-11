package com.example.tumiweb.application.constants;

public class DevMessageConstant {

  public static final class Common {
    private Common() {
    }

    public static final String NOT_FOUND_OBJECT_BY_ID = "Can not find %s by id = %s";
    public static final String DATA_WAS_DELETE = "This object id = %s was delete";
    public static final String DATA_WAS_DISABLE = "This object id = %s was disable";
    public static final String NO_DATA_SELECTED = "No data select result";
    public static final String DUPLICATE_NAME = "Duplicate name = %s";
    public static final String FILE_IS_EMPTY = "File is empty";
  }

  public static final class Help {
    private Help() {
    }

    public static final String USER_WAS_DELETE = "User id = %s was delete. Can not create help object";
  }

  public static final class Answer {
    private Answer() {
    }

    public static final String FULL_ANSWER_FOR_THIS_QUESTION = "Full answer for this question";
  }

  public static final class Gift {
    private Gift() {
    }

  }

  public static final class GiftOrder {
    private GiftOrder() {
    }

    public static final String CAN_NOT_CREATE_GIFT_ORDER = "Can not create gift order";
  }

  public static final class Role {
    private Role() {
    }

    public static final String NOT_FOUND_ROLE_BY_NAME = "Can not find role by name = %s";
  }

  public static final class User {
    private User() {
    }

    public static final String NOT_FOUND_USER_BY_USERNAME = "Can not find user by username = %s";
    public static final String NOT_FOUND_USER_BY_EMAIL = "Can not find user by email = %s";
    public static final String CAN_NOT_CREATE_USER_BY_USERNAME = "Can not create user because duplicate username = %s";
    public static final String CAN_NOT_CREATE_USER_BY_EMAIL = "Can not create user because duplicate email = %s";
    public static final String NOT_FOUND_USER_BY_TOKEN_PASS = "Can not find user by token reset pass = %s";
  }




}
