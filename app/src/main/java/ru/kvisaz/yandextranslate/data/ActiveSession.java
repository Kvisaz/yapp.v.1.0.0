package ru.kvisaz.yandextranslate.data;

import ru.kvisaz.yandextranslate.data.rest.models.Languages;

public class ActiveSession {
    private static Languages mLanguages;

    public static Languages getLanguages() {
        return mLanguages;
    }

    public static void setLanguages(Languages languages) {
        mLanguages = languages;
    }
}
