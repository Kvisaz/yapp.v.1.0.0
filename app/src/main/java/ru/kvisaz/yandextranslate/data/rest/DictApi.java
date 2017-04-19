package ru.kvisaz.yandextranslate.data.rest;

public class DictApi {
    public static final String BASE_URL = "https://dictionary.yandex.net/api/v1/dicservice.json/";
    public static final String KEY_PARAM = "key";

    public static final String LOOKUP_PATH = "lookup";

    public static final String[] LOOKUP_UI_VALUES = new String[]{"en", "ru", "uk", "tr"};
    public static final String LOOKUP_UI_DEFAULT_VALUE = "ru";
}
