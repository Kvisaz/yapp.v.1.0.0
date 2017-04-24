package ru.kvisaz.yandextranslate.common.utils;

import java.util.List;

public class StringUtils {
    public static final String EMPTY_STRING = "";

    public static String getEmptyIfNull(String input) {
        if (input == null) return EMPTY_STRING;
        return input;
    }

    public static String joinToString(List<String> strings, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s).append(delimiter);
        }
        sb.delete(sb.length() - delimiter.length(), sb.length());
        return sb.toString();
    }

    /*
    *    remove whitespaces, multiple \n
    * */
    public static String cleanInput(String text) {
        return text.trim().replaceAll("[\n]{2,}", "\n");
    }

    public static boolean isEmptyString(String text) {
        return text == null || text.length() == 0;
    }

}