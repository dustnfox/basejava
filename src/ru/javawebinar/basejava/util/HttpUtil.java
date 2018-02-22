package ru.javawebinar.basejava.util;

public class HttpUtil {
    public static boolean isEmpty(String val) {
        return val == null || val.trim().isEmpty();
    }
}
