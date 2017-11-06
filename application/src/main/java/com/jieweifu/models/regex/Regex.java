package com.jieweifu.models.regex;

public class Regex {
    public static final String DATE_REX = "([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})";
    public static final String PHONE_REX = "^1([34578])\\d{9}$";
    public static final String USERNAME_REX = "^[a-zA-Z0-9_-]{4,16}$";
    public static final String PASSWORD_REX = "^[a-zA-Z0-9]{6,16}$";
    public static final String EMAIL_REX = "/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$/;";
    public static final String NOTNULL_REX = "^\\s*$";
}
