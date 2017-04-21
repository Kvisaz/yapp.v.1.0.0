package ru.kvisaz.yandextranslate.common.utils;

import java.util.List;

public class StringUtils {
    public static final String EMPTY_STRING = "";

    public static String getEmptyIfNull(String input) {
        if (input == null) return EMPTY_STRING;
        return input;
    }

    public static String joinToString(List<String> strings, String delimiter){
        StringBuilder sb = new StringBuilder();
        for(String s: strings){
            sb.append(s).append(delimiter);
        }
        sb.deleteCharAt(sb.length()-1); // delete last delimiter
        return sb.toString();
    }
}