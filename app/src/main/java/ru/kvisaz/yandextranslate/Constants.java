package ru.kvisaz.yandextranslate;

public class Constants {
    public static final String API_BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/";

    public static final String API_LANGUAGES_DATA_PATH = "getLangs";

    public static final String API_LANGUAGES_KEY_PARAM = "key";
    public static final String API_LANGUAGES_KEY_VALUE = "trnsl.1.1.20170412T165358Z.335f8c118a0f6264.995d68cbab307e5d1997559dd6f747ba6cdab41d";

    public static final String API_LANGUAGES_UI_PARAM = "ui";
    public static final String API_LANGUAGES_UI_VALUE = "ru";

    public static final String API_DETECT_PATH = "detect";
    public static final String API_DETECT_TEXT_PARAM = "text";

    public static final String API_TRANSLATE_PATH = "translate ";
    public static final String API_TRANSLATE_TEXT_PARAM = "text";
    public static final String API_TRANSLATE_LANG_PARAM = "lang";
    public static final String API_TRANSLATE_FORMAT_PARAM = "format";
    public static final String API_TRANSLATE_FORMAT_PLAIN_VALUE = "plain";
    public static final String API_TRANSLATE_FORMAT_HTML_VALUE = "html";
    public static final String API_TRANSLATE_OPTIONS_PARAM = "options";
    public static final String API_TRANSLATE_OPTIONS_DETECT_LANGUAGE_VALUE = "1";

    public static final long READ_TIMEOUT_SECONDS = 15;
    public static final long CONNECT_TIMEOUT_SECONDS = 15;
    public static final String LOG_TAG = "KVISAZ";

    public static final char ALPHA_NON_TRANSPARENT = 255;
    public static final char ALPHA_HALF_TRANSPARENT = 128;

    public static final long START_SCREEN_LOADING_MIN_TIME = 3;

    public static final String DEFAULT_DIRECTION_DELIMITER = "-";

    public static final String DEFAULT_LANGUAGE_CODE = "en";
    public static final String DEFAULT_LANGUAGE_DESCRIPTION = "English";

    public static final String DEFAULT_LANGUAGE_2_CODE = "ru";
    public static final String DEFAULT_LANGUAGE_2_DESCRIPTION = "Русский";

    public static final int MINIMAL_WORD_LENGTH = 1;
    public static final long DELAY_BETWEEN_INPUT_CHANGING_MS = 500;

}
