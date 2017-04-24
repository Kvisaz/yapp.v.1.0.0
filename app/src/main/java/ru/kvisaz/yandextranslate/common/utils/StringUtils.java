package ru.kvisaz.yandextranslate.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {
    public static final String EMPTY_STRING = "";

    public static String getEmptyIfNull(String input) {
        if (input == null) return EMPTY_STRING;
        return input;
    }

    public static String joinToString(List<String> strings, String delimiter) {
        StringBuilder sb = new StringBuilder();
        final int size = strings.size();
        final int maxN = size - 1;
        for (int i = 0; i < size; i++) {
            sb.append(strings.get(i));
            if (i < maxN) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static List<String> convertStringToList(String string, String delimiter) {
        String[] stringArr = string.split(delimiter, -1); // -1 Чтобы пустые строки тоже восстанавливались
        List<String> stringList = Arrays.asList(stringArr);
        return stringList;
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