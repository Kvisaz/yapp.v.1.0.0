package ru.kvisaz.yandextranslate.data;

import ru.kvisaz.yandextranslate.data.models.LanguagesInfo;
import ru.kvisaz.yandextranslate.data.rest.models.LanguagesResponse;

public class ActiveSession {
    private static LanguagesInfo mLanguages;

    public static void saveLanguageData(LanguagesResponse langResponse) {
        mLanguages = new LanguagesInfo(langResponse);
    }

    public static LanguagesInfo getLanguages() {
        return mLanguages;
    }
}
