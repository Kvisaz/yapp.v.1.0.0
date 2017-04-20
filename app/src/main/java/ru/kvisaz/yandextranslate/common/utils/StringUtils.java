package ru.kvisaz.yandextranslate.common.utils;

public class StringUtils {
    public static final String EMPTY_STRING = "";

    public static String getEmptyIfNull(String input) {
        if (input == null) return EMPTY_STRING;
        return input;
    }
}